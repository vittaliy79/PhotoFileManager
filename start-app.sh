#!/bin/bash

# Path to application.properties
properties_file="src/main/resources/application.properties"

# Function to update application.properties with the provided value
update_properties() {
    if grep -q "^photofile.manager.magickhome" "$properties_file"; then
        # Key exists, update it
        sed -i '' "s|^photofile.manager.magickhome.*|photofile.manager.magickhome=$1|" "$properties_file"
    else
        # Key doesn't exist, append it
        echo "photofile.manager.magickhome=$1" >> "$properties_file"
    fi
}

# Check if image.magick.home exists and has a value
image_magick_home=$(grep "^photofile.manager.magickhome=" "$properties_file" | cut -d'=' -f2)

if [ -z "$image_magick_home" ]; then
    echo "Please provide a value for ImageMagick home (photofile.manager.magickhome):"
    read user_input
    update_properties "$user_input"
fi

# Start the Spring Boot application using Gradle
./gradlew bootRun
