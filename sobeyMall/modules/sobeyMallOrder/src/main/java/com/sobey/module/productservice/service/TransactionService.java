package com.sobey.module.productservice.service;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.sobey.module.mallPack.model.MallPackResource;
import com.sobey.module.mallPack.model.MallPackUseRecord;
import com.sobey.module.mallPack.service.MallPackResourceService;
import com.sobey.module.mallPack.service.MallPackUseRecordService;
import com.sobey.module.order.model.Order;
import com.sobey.module.order.service.OrderService;
import com.sobey.module.productservice.entity.charge.Charging;
import com.sobey.module.productservice.entity.charge.Usage;
import com.sobey.module.fegin.balance.request.BalanceService;
import com.sobey.module.fegin.balance.response.Balance;
import com.sobey.module.fegin.product.request.service.ProductService;
import com.sobey.module.fegin.product.response.entity.Metric;
import com.sobey.module.fegin.voucher.request.service.VouAccountService;
import com.sobey.module.fegin.voucher.response.entity.VoucherAccount;
import com.sobey.module.productservice.model.MetricUsage;
import com.sobey.module.productservice.model.ServiceInfo;
import com.sobey.module.productservice.model.UsageStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/19 17:26
 */
@Service
public class TransactionService {

    @Autowired
    private OrderService os;
    @Autowired
    private ServiceInfoService sis;
    @Autowired
    private BalanceService bs;
    @Autowired
    private MetricUsageService mus;
    @Autowired
    private MallPackResourceService mprs;
    @Autowired
    private MallPackUseRecordService mpurs;
    @Autowired
    private UsageStatisticsService uss;
    @Autowired
    private ProductService ps;
    @Autowired
    private VouAccountService vas;

    /**
     * @param order
     * @param metricUsages
     * @param updateList
     * @param balance
     * @param token
     * @param charging     保存每次的使用量，用于后续统计
     */
    @Transactional(rollbackFor = Exception.class)
    public void newOrder(Order order, List<MetricUsage> metricUsages, List<ServiceInfo> updateList, Balance balance, String token, Charging charging, VoucherAccount voucherAccount) {

        if (updateList != null && updateList.size() > 0) {
            sis.updateBatchById(updateList);
        }
        if (metricUsages.size() > 0) {
            mus.insertBatch(metricUsages);
        }
        if (null != balance){
            bs.update(token, balance);
        }
        if (null != charging) {
            Usage[] usages = charging.getUsage();
            List<UsageStatistics> list = new ArrayList<>();
            for (Usage usage : usages) {
                UsageStatistics usageStatistics = new UsageStatistics();
                String metricId = usage.getId();
                List<Metric> metrics = ps.queryPrice(token, metricId,null);
                Metric metric = metrics.get(0);
                usageStatistics.setUuid(IdWorker.get32UUID());
                usageStatistics.setTypeCode(metric.getType());
                usageStatistics.setType(metric.getName());
                usageStatistics.setRequestId(charging.getRequestId());
                usageStatistics.setAccountId(order.getAccountId());
                usageStatistics.setProductId(order.getProductId());
                usageStatistics.setMetricId(usage.getId());
                usageStatistics.setAppId(charging.getAppId());
                usageStatistics.setValue(BigDecimal.valueOf(usage.getValue()).setScale(6, BigDecimal.ROUND_DOWN));
                list.add(usageStatistics);
            }
            uss.insertBatch(list);
        }
        if (null != voucherAccount){
            vas.update(token,voucherAccount);
        }
        os.insert(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleChargeInfo(String token,
                                 Order order,
                                 List<MetricUsage> metricUsages,
                                 Balance balance,
                                 List<ServiceInfo> updateList,
                                 List<MallPackResource> updateResources,
                                 List<MallPackUseRecord> useRecords,
                                 Charging charging,
                                 VoucherAccount vouAccount) {
        if (updateList != null && updateList.size() > 0) {
            sis.updateBatchById(updateList);
        }
        if (null != metricUsages && metricUsages.size() > 0) {
            mus.insertBatch(metricUsages);
        }
        if (null != updateResources && updateResources.size() > 0){
            mprs.updateBatchById(updateResources);
        }
        if (null != balance) {
            bs.update(token, balance);
        }

        if (null != vouAccount){
            vas.update(token,vouAccount);
        }

        for (MallPackUseRecord useRecord : useRecords) {
            mpurs.insert(useRecord);
        }
        if (null != charging) {
            Usage[] usages = charging.getUsage();
            List<UsageStatistics> list = new ArrayList<>();
            String accountId = useRecords.get(0).getAccountId();
            String productId = useRecords.get(0).getProductId();
            for (Usage usage : usages) {
                UsageStatistics usageStatistics = new UsageStatistics();
                List<Metric> metrics = ps.queryPrice(token, usage.getId(),null);
                Metric metric = metrics.get(0);
                usageStatistics.setUuid(IdWorker.get32UUID());
                usageStatistics.setTypeCode(metric.getType());
                usageStatistics.setType(metric.getName());
                usageStatistics.setRequestId(charging.getRequestId());
                usageStatistics.setAccountId(accountId);
                usageStatistics.setProductId(productId);
                usageStatistics.setMetricId(usage.getId());
                usageStatistics.setAppId(charging.getAppId());
                usageStatistics.setValue(BigDecimal.valueOf(usage.getValue()).setScale(6, BigDecimal.ROUND_DOWN));
                list.add(usageStatistics);
            }
            uss.insertBatch(list);
        }
        if (null != order) {
            os.insert(order);
        }

    }


}
