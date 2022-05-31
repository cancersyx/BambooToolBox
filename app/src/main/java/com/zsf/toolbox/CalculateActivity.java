package com.zsf.toolbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by EWorld
 * 2022/5/27
 * 计算器
 */
public class CalculateActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBackIv;
    private TextView mTitleTv;
    private Button mBtn0, mBtn1, mBtn2, mBtn3, mBtn4, mBtn5, mBtn6, mBtn7, mBtn8, mBtn9, mPointBtn;
    private Button mPlusBtn, mMinusBtn, mMulBtn, mDivBtn;
    private Button mEqualBtn, mRootBtn;
    private Button mClearBtn, mBackspaceBtn;
    private EditText mInputEt;
    private boolean clearFlag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText("计算器");
        mBtn0 = findViewById(R.id.btn_0);
        mBtn1 = findViewById(R.id.btn_1);
        mBtn2 = findViewById(R.id.btn_2);
        mBtn3 = findViewById(R.id.btn_3);
        mBtn4 = findViewById(R.id.btn_4);
        mBtn5 = findViewById(R.id.btn_5);
        mBtn6 = findViewById(R.id.btn_6);
        mBtn7 = findViewById(R.id.btn_7);
        mBtn8 = findViewById(R.id.btn_8);
        mBtn9 = findViewById(R.id.btn_9);
        mPointBtn = findViewById(R.id.btn_point);
        mPlusBtn = findViewById(R.id.btn_plus);
        mMinusBtn = findViewById(R.id.btn_minus);
        mMulBtn = findViewById(R.id.btn_multiply);
        mDivBtn = findViewById(R.id.btn_divide);
        mEqualBtn = findViewById(R.id.btn_equal);
        mRootBtn = findViewById(R.id.btn_root);
        mClearBtn = findViewById(R.id.btn_clear);
        mBackspaceBtn = findViewById(R.id.btn_backspace);
        mInputEt = findViewById(R.id.et_input);

        mBackIv.setOnClickListener(this);
        mBtn0.setOnClickListener(this);
        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);
        mBtn4.setOnClickListener(this);
        mBtn5.setOnClickListener(this);
        mBtn6.setOnClickListener(this);
        mBtn7.setOnClickListener(this);
        mBtn8.setOnClickListener(this);
        mBtn9.setOnClickListener(this);
        mPointBtn.setOnClickListener(this);
        mPlusBtn.setOnClickListener(this);
        mMinusBtn.setOnClickListener(this);
        mMulBtn.setOnClickListener(this);
        mDivBtn.setOnClickListener(this);
        mEqualBtn.setOnClickListener(this);
        mRootBtn.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
        mBackspaceBtn.setOnClickListener(this);
        mInputEt.setOnClickListener(this);


    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CalculateActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        String inputContent = mInputEt.getText().toString();
        if (clearFlag) {
            clearFlag = false;
            inputContent = "";
            mInputEt.setText("");
        }
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                mInputEt.setText(inputContent + ((Button) v).getText());
                break;
            case R.id.btn_point:
                if (inputContent.contains(".")) {
                    return;
                }
                mInputEt.setText(inputContent + ((Button) v).getText());
                break;
            case R.id.btn_plus:
            case R.id.btn_minus:
            case R.id.btn_multiply:
            case R.id.btn_divide:
                if (inputContent.contains(" ")) {
                    //含运算符无操作
                    return;
                }
                mInputEt.setText(inputContent + " " + ((Button) v).getText() + " ");
                break;
            case R.id.btn_root:
                if (inputContent.contains(" ")) {
                    //含运算符无操作
                    return;
                }
                if (!(inputContent == null || inputContent.isEmpty())) {// 若不为空，无操作（保证根号在最前面）
                    return;
                }
                mInputEt.setText(inputContent + " " + ((Button) v).getText() + " ");
                break;
            case R.id.btn_backspace:
                if (inputContent != null && !inputContent.equals("")) { // 不为null或""，删除最后一位
                    mInputEt.setText(inputContent.substring(0, inputContent.length() - 1));
                }
                break;
            case R.id.btn_clear:
                mInputEt.setText("");
                break;
            case R.id.btn_equal:
                calculateResult();
                clearFlag = true;
                break;
        }

    }

    private void calculateResult() {
        String exp = mInputEt.getText().toString();// 获取输入框的内容
        if (exp == null || exp.equals("")) { // 为空则不作处理
            return;
        }
        if (!exp.contains(" ")) { // 不包含运算符，不作处理
            return;
        }
        double result = 0;// 初始化运算结果
        String s1 = exp.substring(0, exp.indexOf(" ")); // 截取运算符前面的字符串
        String op = exp.substring(exp.indexOf(" ") + 1, exp.indexOf(" ") + 2); // 截取运算符
        String s2 = exp.substring(exp.indexOf(" ") + 3); // 截取运算符后面的字符串
        if (!s1.equals("") && !s2.equals("")) {// 当 s1 和 s2 均不为空
            double d1 = Double.parseDouble(s1); // 强转为double类型
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                result = d1 + d2;
            } else if (op.equals("-")) {
                result = d1 - d2;
            } else if (op.equals("×")) {
                result = d1 * d2;
            } else if (op.equals("÷")) {
                if (d2 == 0) { // 除数为0，结果设为0
                    result = 0;
                } else {
                    result = d1 / d2;
                }
            }
            if (!s1.contains(".") && !s2.contains(".") && !op.equals("÷")) { // 若s1和s2都是不包含小数点（即都是整数），且不是除法，输出为整数
                int r = (int) result;
                mInputEt.setText(r + "");
            } else {
                mInputEt.setText(result + "");
            }
        } else if (!s1.equals("") && s2.equals("")) { // s1不为空，s2为空，不作处理
            return;
        } else if ((s1.equals("") || s1 == null) && !s2.equals("")) { // s1为空，s2不空，把s1当0处理
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                result = 0 + d2;
            } else if (op.equals("-")) {
                result = 0 - d2;
            } else if (op.equals("×")) {
                result = 0 * d2;
            } else if (op.equals("÷")) {
                result = 0;
            }
        } else if (s1.equals("") && s2.equals("")) { // s1、s2都为空
            mInputEt.setText("");
        }

    }
}
