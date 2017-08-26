#!/usr/bin/env bash

setup() {
    ORIG_WD=$(pwd)
    source ./gradle-tab-completion.bash
    CASHE_FILE="testCache"
}

teardown() {
    rm -f $CASHE_FILE

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
    mkdir build

    assertEquals 2 $(hasCacheFiles)

    echo "atask" > ./build/tasks.cache
    assertEquals 2 $(hasCacheFiles)

    echo "--flag" > ./build/cmdlineflags.cache
    assertEquals 0 $(hasCacheFiles)
}

# Building Cache
#------------------------------------------------------------

# Reading Cache
#------------------------------------------------------------

# test__should_hide_flags_when_reading_cache_for_task() {
#     tasks='testA testB btest module:project'
#     commands="$tasks -h -? --help --version"
#     writeTasksToCache "$commands"

#     result=$(getCommandsForCurrentDirFromCache)

#     assertEquals "$tasks" "$result"
# }

# test__should_show_dashed_tasks() {
#     tasks='testA testB btest test-api test-bl module:project'
#     commands="$tasks -h -? --help --version"
#     writeTasksToCache "$commands"

#     result=$(getCommandsForCurrentDirFromCache)

#     assertEquals "$tasks" "$result"
# }

# test__should_hide_double_dash_commands_when_current_prefix_is_dash() {
#     commands='testA btest module:project -h -? --help --version testB'
#     writeTasksToCache "$commands"

#     result=$(getCommandsForCurrentDirFromCache '-')

#     assertEquals "-h -?" "$result"
# }

# test_should_read_and_write_from_cache_correctly() {
#     tasks='testA testB btest module:project'
#     writeTasksToCache $tasks

#     result=$(getCommandsFromCache)

#     assertEquals "$tasks" "$result"
# }

# test__reading_from_empty_cache_should_return_empty_string() {
#     local result=$(readCacheForCwd)

#     assertEquals '' $result
# }
