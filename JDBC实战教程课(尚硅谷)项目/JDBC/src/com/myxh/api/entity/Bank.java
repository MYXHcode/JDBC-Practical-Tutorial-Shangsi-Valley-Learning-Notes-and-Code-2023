package com.myxh.api.entity;

import java.math.BigDecimal;

/**
 * @author MYXH
 * @date 2023/5/15
 */
public class Bank
{
    private int id;
    private String account;
    private BigDecimal money;

    public Bank()
    {

    }

    public Bank(String account, BigDecimal money)
    {
        this.account = account;
        this.money = money;
    }

    public Bank(int id, String account, BigDecimal money)
    {
        this.id = id;
        this.account = account;
        this.money = money;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public BigDecimal getMoney()
    {
        return money;
    }

    public void setMoney(BigDecimal money)
    {
        this.money = money;
    }

    @Override
    public String toString()
    {
        return "Bank{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", money=" + money +
                '}';
    }
}
