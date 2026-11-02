# PL-A4

This program opens an image, replaces every square of pixels with the average color of that square, and shows the image changing as each square is finished.

## Setup

Install any JDK from Java 8 upward, including Java 17 and newer. `java` and `javac` must be on `PATH`. The compiled program uses only Java 8 language and libraries, so the same class files run on Java 8, Java 17, and later versions. The commands are the same on Windows and macOS.

From the project directory, with JDK 9 or newer:

Windows:

```text
javac --release 8 -d out src\Main.java src\Launch.java src\band\*.java src\cli\*.java src\image\*.java src\process\*.java src\view\*.java src\window\*.java src\worker\*.java
java -cp out Main image.jpg 20 S
```

macOS:

```text
javac --release 8 -d out src/Main.java src/Launch.java src/band/*.java src/cli/*.java src/image/*.java src/process/*.java src/view/*.java src/window/*.java src/worker/*.java
java -cp out Main image.jpg 20 M
```

JDK 8 does not support `--release`. Use `-source 8 -target 8` instead of `--release 8` in the `javac` command above. If no Java is installed at all, install any JDK 8 or newer first.

## Arguments

The program takes three arguments:

```text
java Main <image> <square size> <S|M>
```

- **File name.** Any image file, such as JPEG, PNG, GIF, or BMP. There is no limit on its width or height. It can be your own image, or one of the files currently in `img/`.
- **Square size.** The side length, in pixels, of each square that is averaged. It must be a positive whole number.
- **Mode.** `S` processes the image on one thread, from left to right and from top to bottom. `M` does the same walk in parallel horizontal bands. The number of bands is the number of CPU cores, and it is never larger than the number of square rows.

Example:

```text
java -cp out Main image.jpg 5 S
```

A missing argument, a square size that is not a positive whole number, or a mode other than `S` or `M` prints the error and asks for that value again. `Y` is not a mode, so the program asks for `S` or `M`. A missing file or a file that is not an image prints the error and asks for the file again. The square size and mode already given are kept.

When the file name is left out, the program lists whatever files are currently in `img/` and also accepts the path of your own image.

With a square size of 20, each 20 by 20 block becomes one flat color. A block on the right or bottom edge is smaller when the image size is not a multiple of the square size, and that smaller block is averaged only over the pixels that exist.

## Output

When processing finishes, or when the window is closed, the program writes the averaged image to `result.jpg` in the working directory. That file has the original width and height. Closing the window finishes the square already in progress, then saves the image as it is.

## Display

The window shows the photograph first, then updates it square by square. It uses the largest area the screen can give the frame. If the photograph is larger than that area, the window scales it down and keeps the same width-to-height ratio. A photograph that already fits is shown at its original size, with no scaling up. The scaled picture is only what you see. Each average is calculated from the original pixels, and `result.jpg` is saved at the original width and height.

## Check

Both modes were run on `img/edge.jpg`, a 25 by 17 JPEG, with square size 10. That leaves a 5-pixel column on the right and a 7-pixel row on the bottom. Single-thread and multi-thread produced the same pixels, including those partial squares, and both matched the average of each square in the loaded image. The menu was also given a square size of 0, the mode `Y`, and a missing file. Each of those answers was asked again, and the program stayed open.
