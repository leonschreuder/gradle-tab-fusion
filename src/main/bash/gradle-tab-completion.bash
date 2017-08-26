# References
# https://gist.github.com/nolanlawson/8694399/      # Initial Gist
# https://github.com/eriwen/gradle-completion       # Other completion engine I found later


_TASKS_CACHE_FILE="tasks.cache"
_COMMANDLINEFLAGS_CACHE_FILE="cmdlineflags.cache"
_BUILD_FOLDER="build"   #TODO: Should this be configurable?


# Main
#================================================================================

_gradle() {
    local cur prev commands

    _get_comp_words_by_ref -n : cur         # gets current word without being messed up by ':'
    _get_comp_words_by_ref -n : -p prev

    buildCacheIfRequired

    commands="$(getCommandsForCurrentDirFromCache $cur)"
    setCompletionFor "$prev" "$cur" "$commands"
}


setCompletionFor() {
    local prev="$1"
    local cur="$2"
    local commands="$3"

    # >&2 echo -e "\nprev=$prev cur=$cur commands=$commands"

    case "$prev" in
        # Commands followed by a file-path
        -I|--init-script|-c|--settings-file|-b|--build-file)
            COMPREPLY=( $(compgen -f -- $cur))
            ;;
        # Commands followed by a folder-path
        -g|--gradle-user-home|--include-build|-p|--project-dir|--project-cache-dir)
            COMPREPLY=( $(compgen -d -- $cur))
            ;;
        --console)
            COMPREPLY=( $(compgen -W 'plain auto rich' -- $cur))
            ;;
        *)
            COMPREPLY=( $(compgen -W "${commands}"  -- $cur))
            # ;;
    esac

    # Prevents recursive completion when contains a ':' (Not available in tests)
    [[ -n "$(type -t __ltrim_colon_completions)" ]] && __ltrim_colon_completions "$cur"
}



# Caching
#================================================================================

buildCacheIfRequired() {
    if [[ $(hasCacheFiles) -ne 0 ]]; then

        local msg="  (Caches not found. Telling gradle to generate cache...)"
        local msgLength=${#msg}
        backspaces=$(printf '\b%.0s' $(seq 1 $msgLength))
        spaces=$(printf ' %.0s' $(seq 1 $msgLength))

        echo -en "$msg$backspaces"      # Prints the message and moves the cursor back

        requestGradleBuildsCaches

        echo -en "$spaces$backspaces"   # Overwrites the message with spaces, moving the cursor back again
    fi
}

hasCacheFiles() {
    [[ -s $(getTasksCacheFile) && -s $(getFlagsCacheFile) ]] && echo 0 || echo 2
}

getTasksCacheFile() {
    echo ./$_BUILD_FOLDER/$_TASKS_CACHE_FILE
}

getFlagsCacheFile() {
    echo ./$_BUILD_FOLDER/$_COMMANDLINEFLAGS_CACHE_FILE
}


# Building Cache
#------------------------------------------------------------

requestGradleBuildsCaches() {
    # Outputs nothing unless errors occurr
    $(getGradleCommand) cacheTaskList cacheCommandlineFlags  --console plain --quiet --offline 
}

getGradleCommand() {
    local gradle_cmd='gradle'
    if [[ -x ./gradlew ]]; then
        gradle_cmd='./gradlew'
    fi
    echo $gradle_cmd
}

# Reading Cache
#------------------------------------------------------------

getCommandsForCurrentDirFromCache() {
    currentInput=$1

    # Only show dash commands when expicitly typed to keep it simple
    if [[ $currentInput == "-"* ]]; then
        commands=$(readFlagsCache)
        if [[ $currentInput != "--"* ]]; then
            commands=$(filterSingleDashCommands "$commands")
        # else
            # The user typed a double dash already, completion will filter the single-dashed results out for us
        fi
    else
        commands=$(readTaskCache)
    fi
    # >&2 echo -e "\n commands=$commands"

    echo $commands
}

readTaskCache() {
    local cacheFile="$(getTasksCacheFile)"
    if [ -s $cacheFile ]; then
        while read cacheLine || [[ -n $cacheLine ]]; do
            echo $cacheLine
            return 0
        done < "$cacheFile"
    fi
}

readFlagsCache() {
    local cacheFile="$(getFlagsCacheFile)"
    if [ -s $cacheFile ]; then
        while read cacheLine || [[ -n $cacheLine ]]; do
            echo $cacheLine
            return 0
        done < "$cacheFile"
    fi
}

filterSingleDashCommands() {
    # couldn't find a simple cross-platform solution to filter these out, so loop to get only single dashed
    result=''
    for singleCommand in $1; do
        if [[ $singleCommand == -* &&  $singleCommand != --* ]]; then
            if [[ $result == '' ]]; then
                result="$singleCommand"
            else
                result="$result $singleCommand"
            fi
        fi
    done
    echo $result
}

# Should be available by default but was missing in git-bash.
__ltrim_colon_completions() {
    if [[ "$1" == *:* && ( ${BASH_VERSINFO[0]} -lt 4 || ( ${BASH_VERSINFO[0]} -ge 4 && "$COMP_WORDBREAKS" == *:* ) ) ]]; then
        local colon_word=${1%${1##*:}};
        local i=${#COMPREPLY[*]};
        while [ $((--i)) -ge 0 ]; do
            COMPREPLY[$i]=${COMPREPLY[$i]#"$colon_word"};
        done;
    fi
}


# Define the completion
#================================================================================

complete -o bashdefault -F _gradle gradle
complete -o bashdefault -F _gradle gradlew
complete -o bashdefault -F _gradle ./gradlew

if hash gw 2>/dev/null || alias gw >/dev/null 2>&1; then
    complete -o bashdefault -F _gradle gw
fi

