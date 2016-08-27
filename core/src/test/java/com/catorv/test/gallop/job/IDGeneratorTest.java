package com.catorv.test.gallop.job;

import com.catorv.gallop.job.IDGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by cator on 8/9/16.
 */
public class IDGeneratorTest {

	@Test
	public void testJobID() {
		Set<String> set = new HashSet<>();
		for (int i = 0; i < 100000; i++) {
			String id = IDGenerator.nextJobID();
			set.add(id);
		}

		Assert.assertEquals(100000, set.size());
		System.out.println("Job ID: " + IDGenerator.nextJobID());
	}

	@Test
	public void testTaskID() {
		Set<String> set = new HashSet<>();
		for (int i = 0; i < 100000; i++) {
			String id = IDGenerator.nextTaskID();
			set.add(id);
		}

		Assert.assertEquals(100000, set.size());
		System.out.println("Task ID: " + IDGenerator.nextTaskID());
	}

}
