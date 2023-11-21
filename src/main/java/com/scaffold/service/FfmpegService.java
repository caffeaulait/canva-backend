package com.scaffold.service;
import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.io.IOException;

public interface FfmpegService {
    public void extractAudio() throws IOException;

}
