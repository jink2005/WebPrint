/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webprint.ocxprint;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import java.util.ArrayList;

/**
 *
 * @author ares
 */
public class JacobActiveX {

    public static Object run(String clsId, String methodName, String params) {
        ComThread.InitMTA(true);
        ActiveXComponent com = new ActiveXComponent(clsId);
        Dispatch disp = com.getObject();
        ArrayList objectArray = new ArrayList();
        objectArray.add(params);
        Dispatch.callSub(disp, methodName, new Variant(params));
        //释放线程
        ComThread.Release();
        return null;
    }
}
