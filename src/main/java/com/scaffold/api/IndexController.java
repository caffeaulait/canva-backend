package com.scaffold.api;

import com.scaffold.service.impl.FfmpegServiceImpl;
import com.scaffold.service.FfmpegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
public class IndexController {
    @Autowired
    private FfmpegService ffmpegService;

    @GetMapping("index")
    public ResponseEntity<String> helloWorld(){

        return ResponseEntity.ok("Nice, here is index page!");
    }

    @GetMapping("getVideo")
    public ResponseEntity<String> getVideo() throws IOException {
        ffmpegService.extractAudio();
        return ResponseEntity.ok("Nice, here is index page!");
    }
}
