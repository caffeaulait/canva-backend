package com.scaffold.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import com.scaffold.service.FfmpegService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FfmpegServiceImpl implements FfmpegService {

    public AmazonS3 s3Client;

    @Autowired
    public void setS3Client(@Value("${aws.accessKey}") String accessKey,
        @Value("${aws.secretKey}") String secretKey,
        @Value("${aws.region}") String region) {
        System.out.println(secretKey);
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.fromName(region))
            .build();
    }

    public byte[] downloadFile(String bucketName, String key) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, key);
        byte[] fileBytes = IOUtils.toByteArray(s3Object.getObjectContent());
        return fileBytes;
    }

    public void extractAudio() throws IOException {
        // Download the video file from S3
        byte[] videoBytes = downloadFile("canva-media","taylor.mp4");

        // Create a frame grabber for the video
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new ByteArrayInputStream(videoBytes));
        grabber.start();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("audio.wav", 1);
        recorder.setSampleRate(grabber.getSampleRate());
        recorder.start();

        // Extract the audio from the video
        Frame frame;
        while ((frame = grabber.grab()) != null) {
            // use s16le for example. so select ShortBuffer to receive the sample
            if (frame.samples != null) {
                ShortBuffer sb = (ShortBuffer) frame.samples[0];
                short[] shorts = new short[sb.limit()];
                sb.get(shorts);
                //Split the LRLRLR to LLL in left channel and RRR int right channel
                Frame clonedFrame = frame.clone();
                ShortBuffer leftSb = ShortBuffer.allocate(sb.capacity());
                clonedFrame.samples = new Buffer[]{leftSb};
                clonedFrame.audioChannels = 1;

                for (int i = 0; i < shorts.length; i++) {
                    leftSb.put(shorts[i]);
                }
                // reset the buffer to read mode
                leftSb.rewind();
                recorder.record(clonedFrame);
            }
        }

        // Release the frame grabber
        grabber.stop();
        recorder.stop();
        grabber.release();
    }
}
