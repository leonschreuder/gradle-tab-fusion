#!/usr/bin/env bash

setup() {
    ORIG_WD=$(pwd)
    source ./gradle-tab-completion.bash
    mkdir build
}

teardown() {
    rm -rf build
    rm -f gradlew build.gradle settings.gradle
}



# Main
#================================================================================

test__completion_should_return_all_when_empty() {
    setCompletionFor "" "" "taskA taskB Ctask"
    assertEquals 'taskA taskB Ctask' "${COMPREPLY[*]}"
}

test__completion_should_complete_from_prefix() {
    setCompletionFor "" "tas" "taska taskb ctask"
    assertEquals 'taska taskb' "${COMPREPLY[*]}"
}

test__completion_should_support_colons() {
    # completes full-tasks before colon
    setCompletionFor "" "taska" "taska:tasks taska:dependencies"
    assertEquals 'taska:tasks taska:dependencies' "${COMPREPLY[*]}"

    # completes sub-module tasks only, after a colon
    setCompletionFor "" "module:" "module:taska module:taskb"
    assertEquals 'taska taskb' "${COMPREPLY[*]}"
}

# test__completion_should_support_file_path_completion() {
#     # listing files and dirs
#     setCompletionFor "-I" "./t/" ""
#     assertEquals 2 "${#COMPREPLY[*]}"
#     [[ "${COMPREPLY[*]}" == *"./t/help.out"* ]] || fail "'${COMPREPLY[*]}' does not contain ./t/help.out"
#     [[ "${COMPREPLY[*]}" == *"./t/tasks-full.out"* ]] || fail "'${COMPREPLY[*]}' does not contain ./t/tasks-full.out"

#     setCompletionFor "--build-file" "./t/" ""       # Last flag to make sure the case-syntax is correct
#     assertEquals 2 "${#COMPREPLY[*]}"
#     [[ "${COMPREPLY[*]}" == *"./t/help.out"* ]] || fail "'${COMPREPLY[*]}' does not contain ./t/help.out"
#     [[ "${COMPREPLY[*]}" == *"./t/tasks-full.out"* ]] || fail "'${COMPREPLY[*]}' does not contain ./t/tasks-full.out"
# }

# test__completion_should_support_dir_path_completion() {
#     # listing dirs only
#     setCompletionFor "-g" "" ""
#     assertEquals '.git t' "${COMPREPLY[*]}"

#     setCompletionFor "--project-cache-dir" "" ""    # Last flag to make sure the case-syntax is correct
#     assertEquals '.git t' "${COMPREPLY[*]}"
# }

test__should_support_default_gradle_installation() {
    local result=$(getGradleCommand)
    assertEquals 'gradle' $result
}

test__should_support_gradle_wrapper() {
    echo '#!/bin/bash' > gradlew    # Hashbang to make it executable in git-bash
    chmod +x ./gradlew              # For *nix

    local result=$(getGradleCommand)

    assertEquals './gradlew' $result
}

# Caching
#================================================================================

test__should_be_able_to_see_if_has_cache_files() {
    assertEquals 2 $(hasCacheFiles)

    echo "atask" > $(getTasksCacheFile)
    assertEquals 2 $(hasCacheFiles)

    echo "--flag" > $(getFlagsCacheFile)
    assertEquals 0 $(hasCacheFiles)
}

# Reading Cache
#------------------------------------------------------------

test__should_hide_flags_when_reading_cache_for_task() {
    tasks='testA testB btest test-api test-bl module:project'
    flags="-h -? --help --version"
    echo "$tasks" > $(getTasksCacheFile)
    echo "$flags" > $(getFlagsCacheFile)

    result=$(getCommandsForCurrentDirFromCache)

    assertEquals "$tasks" "$result"
}

test__should_show_single_dash_commands() {
    tasks='testA testB btest test-api test-bl module:project'
    flags="-h -? --help --version"
    echo "$tasks" > $(getTasksCacheFile)
    echo "$flags" > $(getFlagsCacheFile)

    result=$(getCommandsForCurrentDirFromCache '-')

    assertEquals "-h -?" "$result"
}
