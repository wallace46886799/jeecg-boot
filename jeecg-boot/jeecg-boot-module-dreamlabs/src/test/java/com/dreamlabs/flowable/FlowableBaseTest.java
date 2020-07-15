package com.dreamlabs.flowable;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@Transactional
public abstract class FlowableBaseTest {

    @Before
    public void setUp() {
    }
}

