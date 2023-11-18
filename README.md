
# PhotoFileManager

Photo File Manager helps to remove unselected photos after retouching. Just specify the path of your raw photos and the subfolder in that folder where you have results after retouching. 

The app will compare two folders and show all files grouped by the subfolder files that have been retouched. You can then move or remove files that were not selected to be retouched.

The project is using the ImageMagick as the CR2 conversion tool. 

### Install ImageMagick
First, you need to install ImageMagick on the server where your application will run.

 - Windows: Download and install from the [ImageMagick download page](https://imagemagick.org/script/download.php).
 - Linux: Usually available through package managers. For example, on
   Ubuntu, you can install it via `sudo apt-get install imagemagick`.
 - macOS: Install via Homebrew with `brew install imagemagick` or download
   from the [website](https://imagemagick.org/script/download.php).

Then configure the path to the ImageMagick in the application.properties file like `image.magick.home=PATH_TO_IMAGE_MAGICK_FOLDER`

Run the app using the `./start-app.sh` - for the first time, the app will ask for the path to the ImageMagick app if not specified earlier.
