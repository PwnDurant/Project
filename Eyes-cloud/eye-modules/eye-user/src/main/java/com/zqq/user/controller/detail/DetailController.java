package com.zqq.user.controller.detail;


import com.zqq.common.core.controller.BaseController;
import com.zqq.common.core.domain.R;
import com.zqq.user.domain.eye.vo.EyeVO;
import com.zqq.user.service.eye.impl.EyeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eye")
public class DetailController extends BaseController {

    @Autowired
    private EyeServiceImpl eyeService;

    /**
     * 根据疾病名称查看具体信息
     * @param name 疾病名称
     * @return 对应疾病的信息
     */
    @GetMapping("/detail")
    public R<EyeVO> detail(String name){
        return R.ok(eyeService.detail(name));
    }

}
