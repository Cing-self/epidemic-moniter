package com.cjh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.bean.DataBean;
import com.cjh.mapper.DataMapper;
import com.cjh.service.DataService;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl
        extends ServiceImpl<DataMapper, DataBean>
        implements DataService {


}
