/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospringmvc.sandbox;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Iuliana Cosmina on 02/08/2020
 */
public class SimpleStreamsTest {

	List<Ball> bucket = List.of(
			new Ball("BLUE", 9), new Ball("RED", 4),
			new Ball("GREY", 3), new Ball("CYAN", 2),
			new Ball("PINK", 1), new Ball("BLUE", 5),
			new Ball("GREEN", 6), new Ball("RED", 8),
			new Ball("BLUE", 7), new Ball("BLUE", 3)
	);

	@Test
	void testImperative(){
		int sum = 0;
		for(Ball ball : bucket) {
			if (ball.getColor().equals("BLUE") && ball.getDiameter() >= 3) {
				ball.setColor("RED");
				sum += ball.getDiameter();
			}
		}
		assertEquals(24, sum);
	}

	Predicate<Ball> predicate = ball -> ball.getColor().equals("BLUE") && ball.getDiameter() >= 3;
	Function<Ball, Ball> redBall = ball -> new Ball("RED", ball.getDiameter());
	Function<Ball, Integer> quantifier = Ball::getDiameter;

	@Test
	void testDeclarative(){
		int sum  = bucket.stream()
				.filter(predicate)
				.map(redBall)
				.map(quantifier)
				.reduce(0, Integer::sum);
		assertEquals(24, sum);
	}

	Subscriber<Integer> subscriber =  new BaseSubscriber<>() {
		@Override
		protected void hookOnNext(Integer sum) {
			System.out.println("Diameter sum is " + sum);
			assertEquals(24, sum);
		}
	};

	@Test
	void testReactive(){
		Flux.fromIterable(bucket)  // Flux<Ball>
				.filter(predicate) // Flux<Ball>
				.map(redBall) // Flux<Ball>
				.map(quantifier)  // Flux<Integer>
				.reduce(0, Integer::sum) // Mono<Integer>
				.subscribe(subscriber);
	}

	@Test
	void testConcat(){
		Flux<Ball> red =  Flux.just(new Ball("RED", 1), new Ball("RED", 2), new Ball("RED", 3));
		Flux<Ball> blue =  Flux.just(new Ball("BLUE", 4), new Ball("BLUE", 5), new Ball("BLUE", 6));
		Flux<Integer> numbers = Flux.just(5, 6, 9, 8);

		Flux.concat(red,blue).subscribe(ball -> System.out.println(ball.toString()));
		System.out.println("-----------------");
		red.concatWith(blue).subscribe(ball -> System.out.println(ball.toString()));
		System.out.println("-----------------");
		Flux.concat(red, numbers).subscribe(ball -> System.out.println(ball.toString()));
		System.out.println("-----------------");
		Flux.merge(red,blue).subscribe(ball -> System.out.println(ball.toString()));
		System.out.println("-----------------");
		red.mergeWith(blue).subscribe(ball -> System.out.println(ball.toString()));
		System.out.println("-----------------");
		Flux.zip(red,blue).subscribe(tuple -> System.out.println(tuple.toString()));
		System.out.println("-----------------");
		red.zipWith(blue).subscribe(tuple -> System.out.println(tuple.toString()));
	}

	@Test
	void testMerge(){

	}

	@Test
	void testZip(){

	}

}

class Ball{

	String color;
	int diameter;

	public Ball(String color, int diameter) {
		this.color = color;
		this.diameter = diameter;
	}

	@Override
	public String toString() {
		return "Ball{" +
				"color='" + color + '\'' +
				", diameter=" + diameter +
				'}';
	}

	public int getDiameter() {
		return diameter;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}
}