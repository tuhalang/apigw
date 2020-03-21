package com.tuhalang.apigw.common;

public abstract class MyRunnable implements Runnable {

    protected Boolean isFinish;
    protected Object resultObject;

    protected MyRunnable(){
        this.isFinish = false;
        resultObject = null;
    }

    protected abstract void execute();

    @Override
    public void run() {
        isFinish=false;
        execute();
        isFinish=true;
    }

    public Boolean getFinish() {
        return isFinish;
    }

    public void setFinish(Boolean finish) {
        isFinish = finish;
    }

    public Object getResultObject() {
        return resultObject;
    }

    public void setResultObject(Object resultObject) {
        this.resultObject = resultObject;
    }
}
