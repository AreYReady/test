package com.xkj.trade.mvp.master.info.presenter;
import com.xkj.trade.bean_.BeanBaseResponse;
import com.xkj.trade.bean_.BeanMasterClosePosition;
import com.xkj.trade.bean_.BeanMasterOpenPosition;
import com.xkj.trade.mvp.master.info.contract.MasterInfoContract;
import com.xkj.trade.mvp.master.info.model.MasterInfoModelImpl;

/**
* Created by huangsc on 2017/02/24
*/

public class MasterInfoPresenterImpl implements MasterInfoContract.Presenter{
    private MasterInfoContract.View mViewListener;
    private MasterInfoContract.Model mModelListener;
    public MasterInfoPresenterImpl(MasterInfoContract.View mViewListener){
        this.mViewListener=mViewListener;
        this.mModelListener=new MasterInfoModelImpl(this);
    }

    @Override
    public void requestMasterOpenPosition(String masterid, String followid) {
        mModelListener.requestMasterOpenPosition(masterid, followid);
    }

    @Override
    public void responseMasterOpenPosition(BeanMasterOpenPosition beanMasterOpenPosition) {
        mViewListener.responseMasterOpenPosition(beanMasterOpenPosition);
    }

    @Override
    public void requestMasterClosePosition(String masterid, String followid, String pageNo) {
        mModelListener.requestMasterClosePosition(masterid, followid, pageNo);
    }

    @Override
    public void responseMasterClosePosition(BeanMasterClosePosition beanMasterClosePosition) {
        mViewListener.responseMasterClosePosition(beanMasterClosePosition);
    }

    @Override
    public void requestCopyFollow(String masterid,String copyvolume,  String followid) {
        mModelListener.requestCopyFollow(masterid, copyvolume, followid);
    }

    @Override
    public void responseCopyFollow(BeanBaseResponse beanBaseResponse) {
        mViewListener.responseCopyFollow(beanBaseResponse);
    }

    @Override
    public void requestUnCopyFollow(String masterid, String followid) {
        mModelListener.requestUnCopyFollow(masterid, followid);
    }

    @Override
    public void responseUnCopyFollow(BeanBaseResponse beanBaseResponse) {
        mViewListener.responseUnCopyFollow(beanBaseResponse);
    }

    @Override
    public void requestFocus(String masterid, String followid) {
        mModelListener.requestFocus(masterid, followid);
    }

    @Override
    public void responseFocus(BeanBaseResponse beanBaseResponse) {
        mViewListener.responseFocus(beanBaseResponse);
    }

    @Override
    public void requestNoFocus(String masterid, String followid) {
      mModelListener.requestNoFocus(masterid, followid);
    }


    @Override
    public void responseNoFocus(BeanBaseResponse beanBaseResponse) {
        mViewListener.responseNoFocus(beanBaseResponse);
    }
}