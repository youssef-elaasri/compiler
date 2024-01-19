#!/bin/bash


cd "$(dirname "$0")"/../../..

source_directory=./
destination_directory=<destination_directory>

# Create the destination directory if it doesn't exist
mkdir -p "$destination_directory"

# Iterate through all .java files and compile them
for java_file in "$source_directory"/*.java; do
    # Get the filename without extension
    filename=$(basename "$java_file" .java)

    # Compile the .java file and move the .class file to the destination directory
    javac -d "$destination_directory" "$java_file"

    # Check if compilation was successful
    if [ $? -eq 0 ]; then
        echo "Compilation successful for: $filename"
    else
        echo "Compilation failed for: $filename. Please check for errors."
    fi
done

echo "All Java files compiled. Compiled files are in: $destination_directory"
