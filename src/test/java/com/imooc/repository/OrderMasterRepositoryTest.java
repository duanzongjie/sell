package com.imooc.repository;

import com.imooc.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {

    @Autowired
    OrderMasterRepository repository;

    @Test
    public void findOne(){
        OrderMaster master=repository.findOne("1");
        Assert.assertNotNull(master);
    }

    @Test
    public void findByBuyerOpenid() {
        PageRequest pageRequest=new PageRequest(0,2);
        Page<OrderMaster> masters=repository.findByBuyerOpenid("asad",pageRequest);
        masters.getTotalElements();
    }

    @Test
    public void save(){
        //repository.save();
    }
}