
# PhotoFileManager

Project is using the ImageMagick as the CR2 conversion tool

### Install ImageMagick
First, you need to install ImageMagick on the server where your application will run.

 - Windows: Download and install from the [ImageMagick download page](https://imagemagick.org/script/download.php).
 - Linux: Usually available through package managers. For example, on
   Ubuntu, you can install it via `sudo apt-get install imagemagick`.
 - macOS: Install via Homebrew with `brew install imagemagick` or download
   from the [website](https://imagemagick.org/script/download.php).

The configure the path to the ImageMagick in the application.properties file like `image.magick.home=PATH_TO_IMAGE_MAGICK_FOLDER`
