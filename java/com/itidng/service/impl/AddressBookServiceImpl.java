package com.itidng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itidng.mapper.AddressBookMapper;
import com.itidng.pojo.AddressBook;
import com.itidng.result.CurrentId;
import com.itidng.service.AddressBookService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    /**
     * 设置默认地址
     *
     * @param address
     * @return
     */
    public void setDefault(AddressBook address) {
        Long id = address.getId();
        Long userId = CurrentId.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件 根据分类id查询
        queryWrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> addressBooks = this.list(queryWrapper);
        for (AddressBook addressBook : addressBooks) {
            addressBook.setIsDefault(0);
            if (addressBook.getId().equals(id)) {
                addressBook.setIsDefault(1);
            }
            this.updateById(addressBook);
        }
    }
}
