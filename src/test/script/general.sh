#!/bin/bash


cd "$(dirname "$0")"/../../.. || exit 1


source_directory=./src/test/deca/codegen/valid/unprovided/general_tests
destination_directory=./src/test/deca/codegen/valid/unprovided/generated
expected_directory=./src/test/deca/codegen/valid/unprovided/expected
log_directory=./src/test/deca/codegen/valid/unprovided/

# Initialize counters
compile_error_count=0
match_count=0
mismatch_count=0


# Create a log file
log_file="$log_directory/compile.log"
touch "$log_file"

# Create the destination directory if it doesn't exist
mkdir -p "$destination_directory"

log_info() {
    echo "$1" >> "$log_file"
    echo "Generated Output:" >> "$log_file"
    echo "$2" >> "$log_file"
    echo "Expected Output:" >> "$log_file"
    echo "$3" >> "$log_file"
    echo "-----------------------------------" >> "$log_file"
}

# Iterate through all .deca files and compile them
for decaFile in "$source_directory"/*.deca; do
    # Get the filename without extension
    filename=$(basename "$decaFile" .deca)

    # Compile the .deca file and move the .ass file to the destination directory
    decac "$decaFile"
    mv "$source_directory"/"$filename".ass "$destination_directory"


    if [ $? -eq 0 ]; then
      output=$(ima "$destination_directory"/"$filename".ass)
      expected_file="$expected_directory/$filename.expected"

      if [ -e "$expected_file" ]; then
            if diff -q <(echo "$output") "$expected_file" > /dev/null; then
              echo "Match: Output for $filename matches the expected content."
              ((match_count++))

              # Log information about the file
              log_info "$filename" "Compiled correctly" "$(cat "$expected_file")"

            else
              echo "Mismatch: Output for $filename does not match the expected content."
              ((mismatch_count++))

              log_info "$filename" "$output" "$(cat "$expected_file")"
            fi
      else
            echo "Warning: No expected file found for $filename"
      fi

    else
        echo "Compilation failed for: $filename. Please check for errors."
        ((compile_error_count++))
        log_info "$filename" "Compilation failed" ""
    fi

done

# Display summary
echo "Compilation errors: $compile_error_count"
echo "Matches: $match_count"
echo "Mismatches: $mismatch_count"

# Set exit code based on compilation errors or mismatches
if [ "$compile_error_count" -gt 0 ] || [ "$mismatch_count" -gt 0 ]; then
    exit 1
else
    exit 0
fi

