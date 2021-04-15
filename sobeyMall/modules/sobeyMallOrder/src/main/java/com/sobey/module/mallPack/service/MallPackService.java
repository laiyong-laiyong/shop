package com.sobey.module.mallPack.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sobey.module.mallPack.mapper.MallPackMapper;
import com.sobey.module.mallPack.mapper.MallPackResourceMapper;
import com.sobey.module.mallPack.model.MallPack;
import com.sobey.module.mallPack.model.MallPackResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author WCY
 * @createTime 2020/5/20 11:38
 */
@Service
public class MallPackService extends ServiceImpl<MallPackMapper, MallPack> {

    @Autowired
    private MallPackMapper mpm;
    @Autowired
    private MallPackResourceMapper mprm;

    @Transactional(rollbackFor = Exception.class)
    public void createMallPack(MallPack mallPack) {
        mpm.insert(mallPack);
        List<MallPackResource> resources = mallPack.getResources();
        mprm.batchSave(resources);
    }

    public List<MallPack> list(MallPack mallPack) {
        return mpm.list(mallPack);
    }

}
