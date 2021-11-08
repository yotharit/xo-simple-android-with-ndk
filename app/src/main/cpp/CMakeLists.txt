# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project("xo")

add_library(
        xo
        SHARED
        xo.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        xo
        ${log-lib})