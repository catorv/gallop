package com.catorv.test.gallop.util;

import com.catorv.gallop.util.CosineSimilarAlgorithm;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by cator on 12/11/2016.
 */
public class CosineSimilarAlgorithmTest {
	@Test
	public void getSimilarity() throws Exception {
		String s1 = "cator在人来人往的黄昏，你是我人生中的意义vee";
		String s2 = "cator人来人往地黄昏；你是我人生中的意义vee";
		double similarity = CosineSimilarAlgorithm.getSimilarity(s1, s2);
		System.out.println(similarity);
		Assert.assertTrue(similarity > 0.9);

		String s3 = "Lily's American Diner";
		String s4 = "Lily's American Diner（LAD）(美意墨)";
		double similarity2 = CosineSimilarAlgorithm.getSimilarity(s3, s4);
		System.out.println(similarity2);
		Assert.assertTrue(similarity2 > 0.9);
	}

}