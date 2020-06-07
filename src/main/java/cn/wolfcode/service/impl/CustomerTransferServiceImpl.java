package cn.wolfcode.service.impl;

import cn.wolfcode.domain.CustomerTransfer;
import cn.wolfcode.mapper.CustomerMapper;
import cn.wolfcode.mapper.CustomerTransferMapper;
import cn.wolfcode.qo.QueryObject;
import cn.wolfcode.service.ICustomerTransferService;
import cn.wolfcode.util.UserContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerTransferServiceImpl implements ICustomerTransferService {

    @Autowired
    private CustomerTransferMapper customerTransferMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public void save(CustomerTransfer customerTransfer) {
        //针对这销售人员的更新
        customerMapper.updates(customerTransfer.getNewSeller().getId(),
                customerTransfer.getCustomer().getId());

        //添加操作人
        customerTransfer.setOperator(UserContext.getCurrentUser());
        //添加操作的时间
        customerTransfer.setOperateTime(new Date());
        customerTransferMapper.insert(customerTransfer);
    }

    @Override
    public void delete(Long id) {
        customerTransferMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(CustomerTransfer customerTransfer) {
        customerTransferMapper.updateByPrimaryKey(customerTransfer);
    }

    @Override
    public CustomerTransfer get(Long id) {
        return customerTransferMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CustomerTransfer> listAll() {
        return customerTransferMapper.selectAll();
    }

    @Override
    public PageInfo<CustomerTransfer> query(QueryObject qo) {
        PageHelper.startPage(qo.getCurrentPage(),qo.getPageSize()); //对下一句sql进行自动分页
        List<CustomerTransfer> customerTransfers = customerTransferMapper.selectForList(qo); //里面不需要自己写limit
        return new PageInfo<CustomerTransfer>(customerTransfers);
    }
}
