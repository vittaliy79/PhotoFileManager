
# PhotoFileManager

This project was created with 80% help of the ChatGPT.

Photo File Manager helps to remove unselected photos after retouching. Just specify the path of your raw photos and the subfolder in that folder where you have results after retouching. 

The app will compare two folders and show all files grouped by the subfolder files that have been retouched. You can then move or remove files that were not selected to be retouched.

App features:
- supports preview of images when clicking on them
- raw files require conversion and can be loaded separately
- support arrow keys to navigate between images in the preview
- supports ESC to close the preview


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

When the app starts, then open browser with URL: http://localhost:8080/
