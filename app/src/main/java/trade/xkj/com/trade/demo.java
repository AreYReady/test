package trade.xkj.com.trade;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import trade.xkj.com.trade.Utils.view.CustomPortfolio;
import trade.xkj.com.trade.bean.BeanPortfolioData;

public class demo extends AppCompatActivity {
    CustomPortfolio customPortfolio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        customPortfolio=(CustomPortfolio)  findViewById(R.id.cp_portfolio);
        ArrayList<BeanPortfolioData> list=new ArrayList<>();
        int begin=-180;
        for(int i=0;i<6;i++) {
            BeanPortfolioData data = new BeanPortfolioData();
            data.setBeginAngle(begin);
            data.setEndAngle(begin+30);
            begin = data.getEndAngle();
            Log.i("hsc", "onCreate: "+begin);
            data.setSymbol("huangsc");
            data.setType("测试");
            list.add(data);
        }
        customPortfolio.setData(list);
    }
}
