package com.qinbin.p2p.bean;

/**
 * 是 三种数据的”最小公倍数“
 */
public class ProductInfo {




    //FinanceInfo
    // 产品的id
    //public String productId;//: "211000822",
    // 产品名称
    //public String productName;//: "牛稳赚JSEEC61002106",
    // 产品的标签
    //public String prdMark;//: "本息保障",
    // 预期年化收益率
    // public String predictYearRate;//: "6.80",
    // 产品类型
    //public int productType;//: 1,
    // 是否可购买
    // public boolean purchaseFlag;//: false,
    // 卖出进度
    //public String sellPer;//: "100%",
    // 状态码
    //public String statusCode;//: "6",
    // 状态描述
    //public String statusDesc;//: "计息中",
    // 产品的收益和本金到期日期
    //public String timeLimit;//: 365
    // FundInfo
    // public String productId;//: "000692",
    //public String productName;//: "汇添富双利债券C"

    // 基金的标签
    public String fundDesc;//: "债券型",
    // 基金的状态
    public String fundStatus;//: "0",
    // 近一年的收益率，可能是负的
    public String lastYearIncreaseStr;//: "8.21",
    // 起购金额
    public int lowerApplyAmount;//: 1000,
    // 净值
    public float newUnit;//: 1.345,
    // 风险等级
    public int prdRiskLevel;//: 2,

    //    InvestInfo
    public String productId;//: "245001299",
    public String productName;//: "牛变现245001299",
    // 借款id
    public String borrowerId;//: "17521447",
    // 产品标签
    public String prdMark;//: "",
    // 预计年化收益率
    public String predictYearRate;//: "5.80",
    // 产品类型
    public int productType;//: 1,
    // 是否能够继续购买
    public boolean purchaseFlag;//: true,
    // 卖出进度
    public String sellPer;//: "0",
    // 状态码
    public String statusCode;//: "3",
    // 状态描述
    public String statusDesc;//: "立即抢购",
    // 借款期限
    public int timeLimit;//: 219

    // 数据类型，或者view的类型
    /** 0 == FinanceInfo  1 == FundInfo   2 == InvestInfo */
    public int viewType;
    public ProductInfo(FinanceInfo financeInfo) {
        viewType =0;
        this.productId = financeInfo.productId;
        this.productName = financeInfo.productName;
        this.prdMark = financeInfo.prdMark;
        this.predictYearRate = financeInfo.predictYearRate;
        this.productType = financeInfo.productType;
        this.purchaseFlag = financeInfo.purchaseFlag;
        this.sellPer = financeInfo.sellPer;
        this.statusCode = financeInfo.statusCode;
        this.statusDesc = financeInfo.statusDesc;
        this.timeLimit = financeInfo.timeLimit;

    }

    public ProductInfo(FundInfo fundInfo) {
        viewType =1;
        this.productId = fundInfo.productId;
        this.productName = fundInfo.productName;
        this.fundDesc = fundInfo.fundDesc;
        this.fundStatus = fundInfo.fundStatus;
        this.lastYearIncreaseStr = fundInfo.lastYearIncreaseStr;
        this.lowerApplyAmount = fundInfo.lowerApplyAmount;
        this.newUnit = fundInfo.newUnit;
        this.prdRiskLevel = fundInfo.prdRiskLevel;


    }

    public ProductInfo(InvestInfo investInfo) {
        viewType =2;
        this.productId = investInfo.productId;
        this.productName = investInfo.productName;
        this.borrowerId = investInfo.borrowerId;
        this.prdMark = investInfo.prdMark;
        this.predictYearRate = investInfo.predictYearRate;
        this.productType = investInfo.productType;
        this.purchaseFlag = investInfo.purchaseFlag;
        this.sellPer = investInfo.sellPer;
        this.statusCode = investInfo.statusCode;
        this.statusDesc = investInfo.statusDesc;
        this.timeLimit = investInfo.timeLimit;


    }
}
