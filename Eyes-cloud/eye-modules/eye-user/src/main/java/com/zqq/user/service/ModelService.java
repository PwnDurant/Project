package com.zqq.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zqq.common.core.constants.Constants;
import com.zqq.common.core.utils.MultipartInputStreamFileResource;
import com.zqq.common.core.utils.ThreadLocalIUtil;
import com.zqq.user.domain.eye.Eye;
import com.zqq.user.domain.eye.vo.EyeVO;
import com.zqq.user.domain.record.Record;
import com.zqq.user.domain.result.RecordResult;
import com.zqq.user.domain.result.SuccessVO;
import com.zqq.user.domain.user.User;
import com.zqq.user.mapper.eye.EyeMapper;
import com.zqq.user.mapper.record.RecordMapper;
import com.zqq.user.mapper.user.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
public class ModelService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private EyeMapper eyeMapper;

    public SuccessVO uploadImagesFromUrl(String leftImageUrl, String rightImageUrl) throws IOException {
        String url = "http://127.0.0.1:5001/predict";
        int tmp=0;
        // 如果其中一个为空，就用另一个替代
        if ((leftImageUrl == null || leftImageUrl.isEmpty()) && (rightImageUrl != null && !rightImageUrl.isEmpty())) {
            leftImageUrl = rightImageUrl;
            tmp=2;
        } else if ((rightImageUrl == null || rightImageUrl.isEmpty()) && (leftImageUrl != null && !leftImageUrl.isEmpty())) {
            rightImageUrl = leftImageUrl;
            tmp=1;
        }

        // 如果两个都是空，直接抛异常
        if ((leftImageUrl == null || leftImageUrl.isEmpty()) && (rightImageUrl == null || rightImageUrl.isEmpty())) {
            throw new IllegalArgumentException("至少需要传入一张图片链接！");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 加载图片
        if (leftImageUrl != null && !leftImageUrl.isEmpty()) {
            InputStream leftStream = downloadImage(leftImageUrl);
            String leftFileName = getFileNameFromUrl(leftImageUrl);
            body.add("left_image", new MultipartInputStreamFileResource(leftStream, leftFileName));
        }

        if (rightImageUrl != null && !rightImageUrl.isEmpty()) {
            InputStream rightStream = downloadImage(rightImageUrl);
            String rightFileName = getFileNameFromUrl(rightImageUrl);
            body.add("right_image", new MultipartInputStreamFileResource(rightStream, rightFileName));
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
        Map result = response.getBody();
        Record record=setRecord(result,leftImageUrl,rightImageUrl,tmp);



        SuccessVO successVO=new SuccessVO();
        RecordResult recordResult=new RecordResult();
        assert record != null;
        BeanUtils.copyProperties(record,recordResult);
        successVO.setRecordResult(recordResult);
        Eye eye=eyeMapper.selectOne(new LambdaQueryWrapper<Eye>()
                .eq(Eye::getName,recordResult.getResult()));
        EyeVO eyeVO=new EyeVO();
        BeanUtils.copyProperties(eye,eyeVO);
        successVO.setEyeVO(eyeVO);
        return successVO;
    }

    private Record setRecord(Map result,String leftImageUrl, String rightImageUrl,int tmp) {
        if(!isExist()) return null;
        Record record=new Record();
        record.setUserId(ThreadLocalIUtil.get(Constants.USER_ID, Long.class));
        if(tmp==1){
            record.setEyeType("左眼");
            record.setLeftImage(leftImageUrl);
        } else if (tmp==2) {
            record.setEyeType("右眼");
            record.setRightImage(rightImageUrl);
        }else{
            record.setEyeType("两只眼睛");
            record.setLeftImage(leftImageUrl);
            record.setRightImage(rightImageUrl);
        }
        Map.Entry<String, Double> res=getMaxEntry(result);
        record.setResult(res.getKey());
        record.setConfidence(res.getValue().floatValue());
        recordMapper.insert(record);
        return record;
    }

    private Map.Entry<String, Double> getMaxEntry(Map<String, Double> resultMap) {
        return resultMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null); // 如果为空则返回 null
    }

    private boolean isExist() {
        Long userId=ThreadLocalIUtil.get(Constants.USER_ID,Long.class);
        User user=userMapper.selectById(userId);
        return user != null;
    }

    private InputStream downloadImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        } else {
            throw new IOException("图片下载失败，状态码：" + connection.getResponseCode());
        }
    }

    private String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
