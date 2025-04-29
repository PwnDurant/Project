package com.zqq.testchase.model.tools;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class PromptLoader {
    private final ResourceLoader resourceLoader;

    public PromptLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String loadPrompt(String source) throws IOException {
        Resource resource = resourceLoader.getResource(source);
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(bytes, StandardCharsets.UTF_8);
    }
}