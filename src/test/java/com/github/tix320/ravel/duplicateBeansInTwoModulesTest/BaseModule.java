package com.github.tix320.ravel.duplicateBeansInTwoModulesTest;

import com.github.tix320.ravel.api.BeansModule;
import com.github.tix320.ravel.api.UseModule;

@UseModule({MyFirstModule.class, MySecondModule.class})
public class BaseModule implements BeansModule {}
