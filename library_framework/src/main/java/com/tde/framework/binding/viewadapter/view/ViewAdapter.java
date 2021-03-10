package com.tde.framework.binding.viewadapter.view;

import android.view.View;

import androidx.databinding.BindingAdapter;

import com.tde.framework.binding.command.BindingCommand;

public class ViewAdapter {

    /**
     * 回调控件本身
     *
     * @param currentView
     * @param bindingCommand
     */
    @BindingAdapter(value = {"currentView"}, requireAll = false)
    public static void replyCurrentView(View currentView, BindingCommand bindingCommand) {
        if (bindingCommand != null) {
            bindingCommand.execute(currentView);
        }
    }

}
