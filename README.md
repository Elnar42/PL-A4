# PL-A4

This program opens a JPEG, replaces every square of pixels with the average color of that square, and shows the image changing as each square is finished.

## Arguments

The program takes three arguments:

```text
<file name> <square size> <mode>
```

- **File name.** A JPEG file. There is no limit on its width or height.
- **Square size.** The side length, in pixels, of each square that is averaged.
- **Mode.** `S` processes the image on one thread, from left to right and from top to bottom. `M` does the same walk in several regions at the same time. The number of threads follows the number of CPU cores.

Example:

```text
program image.jpg 5 S
```

With a square size of 20, each 20 by 20 block becomes one flat color, including a smaller block along the right or bottom edge when the image size is not a multiple of the square size.

## Output

When processing finishes, the program writes the averaged image to `result.jpg` in the working directory. That file has the original width and height.

## Display

The window shows the photograph first, then updates it square by square. If the image is larger than the screen, the window shows it scaled down to fit. Scaling is only for display. Averaging, and the file written to `result.jpg`, always use the original image.
