package com.github.tix320.ravel.duplicateBeansInTwoModulesTest;

import com.github.tix320.ravel.api.UseModules;

@UseModules(classes = {MyFirstModule.class, MySecondModule.class})
public class BaseModule {}
