package com.sdgvvk.v1.modal;

public class OrderModal {

    public String id;
    public String maxCount;
    public String usedCount;

    public String cpid;
    public String membershipId;
    public String paymentmode;
    public String txndate;

    public String startDate;

    public String endDate;

    public Integer countRemaining;

    public OrderModal(String cpid, String membershipId, String paymentmode, String txnDate) {
        this.cpid = cpid;
        this.membershipId = membershipId;
        this.paymentmode = paymentmode;
        this.txndate = txnDate;
    }


    public OrderModal() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(String maxCount) {
        this.maxCount = maxCount;
    }

    public String getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(String usedCount) {
        this.usedCount = usedCount;
    }

    public String getCpid() {
        return cpid;
    }

    public void setCpid(String cpid) {
        this.cpid = cpid;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getCountRemaining() {
        int max = 0;
        int used = 0;
        int result = 0;
        try {
            max = Integer.parseInt(maxCount);
            used = Integer.parseInt(usedCount);

        } catch (NumberFormatException e) {
            // Handle the exception - Log an error or return an error value
            //return null; // Or throw an exception, depending on your application's needs
        }
        finally {
            result  = max - used;
        }
        return result;
    }
}
