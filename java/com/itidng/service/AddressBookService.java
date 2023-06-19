package com.itidng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itidng.pojo.AddressBook;



public interface AddressBookService extends IService<AddressBook> {
    /**
     * 设置默认地址
     */
    void setDefault(AddressBook addressBook);
}
