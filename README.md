# PL-A4

This program opens a JPEG, replaces every square of pixels with the average color of that square, and shows the image changing as each square is finished.

## Setup

Install JDK 17 or newer. `java` and `javac` must be available on `PATH`.

On Windows, install a JDK from [Adoptium](https://adoptium.net/) or Oracle, then add its `bin` folder to `PATH`. From the project directory:

```text
run.bat image.jpg 20 S
```

On macOS, install a JDK 17 or newer (for example with `brew install openjdk@17`) and make sure its `bin` folder is on `PATH`. From the project directory:

```text
chmod +x run.sh
./run.sh image.jpg 20 M
```

Each script compiles every source file under `src` with `javac --release 17` into `out`, then starts `Main`. The script moves to the project directory first, so `result.jpg` is written there. Give the JPEG as a path that is valid from that directory. Close the window to leave the program. A missing file, a file that is not a JPEG, or bad arguments prints the error and the script exits with status 1.

## Arguments

The program takes three arguments:

```text
<file name> <square size> <mode>
```

- **File name.** A JPEG file. There is no limit on its width or height.
- **Square size.** The side length, in pixels, of each square that is averaged. It must be a positive whole number.
- **Mode.** `S` processes the image on one thread, from left to right and from top to bottom. `M` does the same walk in parallel horizontal bands. The number of bands is the number of CPU cores, and it is never larger than the number of square rows.

Example:

```text
run.bat image.jpg 5 S
```

With a square size of 20, each 20 by 20 block becomes one flat color. A block on the right or bottom edge is smaller when the image size is not a multiple of the square size, and that smaller block is averaged only over the pixels that exist.

## Output

When processing finishes, or when the window is closed, the program writes the averaged image to `result.jpg` in the project directory. That file has the original width and height. Closing the window finishes the square already in progress, then saves the image as it is.

## Display

The window shows the photograph first, then updates it square by square. It uses the largest area the screen can give the frame. If the photograph is larger than that area, the window scales it down and keeps the same width-to-height ratio. A photograph that already fits is shown at its original size, with no scaling up. The scaled picture is only what you see. Each average is calculated from the original pixels, and `result.jpg` is saved at the original width and height.

## Check

Both modes were run on a 25 by 17 JPEG with square size 10. That leaves a 5-pixel column on the right and a 7-pixel row on the bottom. Single-thread and multi-thread produced the same pixels, including those partial squares, and both matched the average of each square in the loaded image. The two saved `result.jpg` files matched as well. A missing file was rejected before a window opened.
