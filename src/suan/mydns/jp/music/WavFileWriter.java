package suan.mydns.jp.music;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class WavFileWriter extends Thread {
    private AudioInputStream audioInputStream;
    private File outputFile;

    private BufferedOutputStream bufOutput = null;
    private BufferedInputStream bufInput = null;

    private int MAX_VALUE;
    private int MIN_VALUE;

    private ByteBuffer bb;

    private int byteOffset;
    private int sampleSizeInByte;

    private static final int INT_BYTE_LENGTH = 4;

    private int n;// write values

    public WavFileWriter(File output, AudioFormat format, long frameLenght) {

        this.outputFile = output;

        try {
            PipedOutputStream outputStream = new PipedOutputStream();
            PipedInputStream inputStream = new PipedInputStream();
            outputStream.connect(inputStream);
            bufOutput = new BufferedOutputStream(outputStream);
            bufInput = new BufferedInputStream(inputStream);

            int sampleSizeInBit = format.getSampleSizeInBits();

            this.sampleSizeInByte = sampleSizeInBit / 8;

            this.bb = ByteBuffer.allocate(INT_BYTE_LENGTH);
            if (format.isBigEndian()) {
                this.bb.order(ByteOrder.BIG_ENDIAN);
                this.byteOffset = INT_BYTE_LENGTH - sampleSizeInByte;
            } else {
                this.bb.order(ByteOrder.LITTLE_ENDIAN);
                this.byteOffset = 0;
            }

            MAX_VALUE = (int) (Math.pow(2, sampleSizeInBit) - 1);
            MIN_VALUE = (int) (-Math.pow(2, sampleSizeInBit));

            audioInputStream = new AudioInputStream(bufInput, format, frameLenght);
            this.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            bufOutput.flush();
            bufOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putFrame(int[] values) throws IOException {
        for (int ch = 0; ch < values.length; ch++) {
            n = Math.min(MAX_VALUE, values[ch]);
            n = Math.max(MIN_VALUE, n);
            this.bb.putInt(0, n);
            bufOutput.write(bb.array(), byteOffset, sampleSizeInByte);
        }
    }

    public void run() {
        try {
            AudioSystem.write(audioInputStream, Type.WAVE, outputFile);
            audioInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
