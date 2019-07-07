import edu.princeton.cs.algs4.Picture;

import java.util.Arrays;

/**
 *
     ASSESSMENT SUMMARY

     Compilation:  PASSED
     API:          PASSED

     Spotbugs:     PASSED
     PMD:          PASSED
     Checkstyle:   PASSED

     Correctness:  31/31 tests passed
     Memory:       6/6 tests passed
     Timing:       18/17 tests passed
 *
 * Created by yixu on 2019/7/6.
 */
public class SeamCarver {

    private Picture picture;

    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new  IllegalArgumentException("picture can't be null");
        this.picture = new Picture(picture);
        this.width = picture.width();
        this.height = picture.height();
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }


    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateColIndex(x);
        validateRowIndex(y);

        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return 1000;

        int leftRGB = picture.getRGB(x-1, y);
        int rightRGB = picture.getRGB(x+1, y);
        int upRGB = picture.getRGB(x, y-1);
        int downRGB = picture.getRGB(x, y+1);

        return Math.sqrt(deltaEnergy(leftRGB, rightRGB) + deltaEnergy(upRGB, downRGB));
    }


    private double deltaEnergy(int color1, int color2) {
        return Math.pow((color1 & 0xff) - (color2 & 0xff), 2)
                + Math.pow(((color1 >> 8) & 0xff) - ((color2 >> 8) & 0xff), 2)
                + Math.pow(((color1 >> 16) & 0xff) - ((color2 >> 16) & 0xff), 2);
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row >= this.height)
            throw new IllegalArgumentException("row index must be between 0 and " + (height() - 1) + ": " + row);
    }

    private void validateColIndex(int col) {
        if (col < 0 || col >= this.width)
            throw new IllegalArgumentException("column index must be between 0 and " + (width() - 1) + ": " + col);
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        // store energies of picture
        double[][] energies = new double[width][height]; // reverse
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energies[i][j] = energy(i, j);  // reverse
            }
        }

        return findSeam(energies, width, height);
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        // store energies of picture
        double[][] energies = new double[height][width];
        for (int row = 0; row < height; row++) {
            for (int j = 0; j < width; j++) {
                energies[row][j] = energy(j, row);
            }
        }

        return findSeam(energies, height, width);
    }

    private int[] findSeam(double[][] energies, int m, int n) {

        // 1.find shortest path
        double[][] energyTo = new double[m][n];
        Arrays.fill(energyTo[0], 1000);

        int[][] indexTo = new int[m][n];

        int minEnergyIdx;
        for (int row = 1; row < m; row++) {
            for (int col = 0; col < n; col++) {

                minEnergyIdx = col;
                if (col > 0 && energyTo[row - 1][col - 1] < energyTo[row - 1][minEnergyIdx])
                    minEnergyIdx = col - 1;
                if (col < n - 1 && energyTo[row - 1][col + 1] < energyTo[row - 1][minEnergyIdx])
                    minEnergyIdx = col + 1;

                indexTo[row][col] = minEnergyIdx;
                energyTo[row][col] = energyTo[row - 1][minEnergyIdx] + energies[row][col];
            }
        }

        // 2.find seam
        int[] seam = new int[m];
        // find seam[m - 1]
        double minEnergy = Integer.MAX_VALUE;
        for (int col = 0; col < n; col++) {
            if (energyTo[m - 1][col] < minEnergy) {
                minEnergy = energyTo[m - 1][col];
                seam[m - 1] = col;
            }
        }
        // find seam[i], 0 <= i < m-1
        for (int row = m - 1; row > 0; row--) {
            seam[row - 1] = indexTo[row][seam[row]];
        }
        return seam;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height <= 1)
            throw new  IllegalArgumentException("height is <= 1");
        if (seam == null)
            throw new  IllegalArgumentException("seam can't be null");
        if (seam.length != width)
            throw new  IllegalArgumentException("seam is wrong length: " + seam.length);
        for (int i = 0; i < width; i++)
            validateRowIndex(seam[i]);
        for (int i = 1; i < width; i++) {
            if (Math.abs(seam[i] - seam[i-1]) > 1)
                throw new  IllegalArgumentException("seam is not a valid seam, gap is > 1");
        }

        Picture newPicture = new Picture(width, --height);

        for (int col = 0; col < width; col++) {
            int row;
            for (row = 0; row < seam[col]; row++) {
                newPicture.setRGB(col, row, picture.getRGB(col, row));
            }
            for (row = seam[col]; row < height; row++) {
                newPicture.setRGB(col, row, picture.getRGB(col, row + 1));
            }
        }

        this.picture = newPicture;
    }


    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width <= 1)
            throw new  IllegalArgumentException("width is <= 1");
        if (seam == null)
            throw new  IllegalArgumentException("seam can't be null");
        if (seam.length != height)
            throw new  IllegalArgumentException("seam is wrong length: " + seam.length);
        for (int i = 0; i < height; i++)
            validateColIndex(seam[i]);
        for (int i = 1; i < height; i++) {
            if (Math.abs(seam[i] - seam[i-1]) > 1)
                throw new  IllegalArgumentException("seam is not a valid seam, gap is > 1");
        }

        Picture newPicture = new Picture(--width, height);

        for (int row = 0; row < height; row++) {
            int col;
            for (col = 0; col < seam[row]; col++) {
                newPicture.setRGB(col, row, picture.getRGB(col, row));
            }
            for (col = seam[row]; col < width; col++) {
                newPicture.setRGB(col, row, picture.getRGB(col + 1, row));
            }
        }

        this.picture = newPicture;
    }


    public static void main(String[] args) {
        //  unit testing (optional)
    }
}
