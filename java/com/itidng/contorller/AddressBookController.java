package com.itidng.contorller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itidng.pojo.AddressBook;
import com.itidng.result.CurrentId;
import com.itidng.result.R;
import com.itidng.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户地址管理
 */
@Slf4j
@RestController
@RequestMapping("addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService mapper;

    /**
     * 展示默认地址
     *
     * @return
     */
    @GetMapping("default")
    public R<AddressBook> getDefaultAddress() {
        Long userId = CurrentId.getCurrentId();
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getIsDefault, 1);
        wrapper.eq(AddressBook::getUserId, userId);
        AddressBook addressBook = mapper.getOne(wrapper);
        if (addressBook != null) {
            return new R<>(1, addressBook, "地址展示成功");
        }
        return new R<>(1, null, "没查找到对应的信息");
    }


    /**
     * 添加地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> addAddress(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(CurrentId.getCurrentId());
        mapper.save(addressBook);
        return new R<>(1, null, "");
    }


    /**
     * 展示地址列表
     *
     * @return
     */
    @GetMapping("list")
    public R<List<AddressBook>> addressList() {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        Long currentId = CurrentId.getCurrentId();
        wrapper.eq(currentId != null, AddressBook::getUserId, currentId);
        List<AddressBook> list = mapper.list(wrapper);
        return new R<>(1, list, "地址展示成功");
    }

    /**
     * 查询相应地址信息
     *
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R<AddressBook> getAddress(@PathVariable("id") Long id) {
        AddressBook addressBook = mapper.getById(id);
        if (addressBook == null) {
            return new R<>(0, null, "没查到相应信息");
        }
        return new R<>(1, addressBook, "查询成功");
    }

    /**
     * 设置默认地址
     *
     * @param address
     * @return
     */
    @PutMapping("default")
    public R<AddressBook> setDefaultAddress(@RequestBody AddressBook address) {

        mapper.setDefault(address);
        return new R<>(1, null, "默认地址设置成功");
    }


    /**
     * 修改地址信息
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> updateAddress(@RequestBody AddressBook addressBook) {
        mapper.updateById(addressBook);
        return new R<>(1, null, "地址信息修改成功");
    }


    /**
     * 地址删除
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> deleteAddressBook(@RequestParam("ids") Long id) {
        mapper.removeById(id);
        return new R<>(1, null, "地址删除成功");
    }
}
