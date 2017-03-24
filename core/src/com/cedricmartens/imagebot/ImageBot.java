package com.cedricmartens.imagebot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.org.apache.bcel.internal.generic.POP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImageBot extends ApplicationAdapter {
	SpriteBatch batch;
	AssetManager assetManager;
	private Image originalImage;
	private Image bestImage;
	private float allTimeBest;
	private List<Image> pop;
	private int generation;
	private long lastBestTime;
	private final int POPULATION = 1000;

	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		generation = 0;
		lastBestTime = System.currentTimeMillis();


		assetManager.load("original.png", Texture.class);
		assetManager.finishLoading();

		Texture texture = assetManager.get("original.png");
		originalImage = new Image(texture);
		pop = new ArrayList<>();
		allTimeBest = Float.MAX_VALUE;
		for(int i = 0; i < POPULATION; i++)
			pop.add(new Image(texture.getWidth(), texture.getHeight()));

		File f = new File("../../../results.ibr");
		try {
			FileOutputStream is = new FileOutputStream(f);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);
			w.write("original.png,"+ texture.getWidth()+"," + texture.getHeight() + "," + POPULATION + ";");
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		doGeneration();
		batch.begin();
		batch.draw(originalImage.getTexture(), 0, 0, 400, 400);
		batch.draw(bestImage.getTexture(), 400, 0, 400, 400);
		batch.draw(pop.get(0).getTexture(), 800, 0, 400, 400);
		batch.end();
	}

	private void doGeneration() {
		generation++;
		Image im = getBestFitnessImage();

		for (int i = 0; i < pop.size(); i++) {
			pop.set(i, new Image(im));
		}
	}

	private Image getBestFitnessImage()
	{
		int smallest = -1;
		float smallestDelta = Float.MAX_VALUE;

		for(int i = 0; i < pop.size(); i++)
		{
			float d = pop.get(i).getFitness(originalImage);

			if(d < smallestDelta) {
				smallestDelta = d;
				smallest = i;
			}
		}


		if(smallestDelta < allTimeBest)
		{
			bestImage = pop.get(smallest);
			allTimeBest = bestImage.getFitness(originalImage);
			long delta = System.currentTimeMillis() - lastBestTime;
			lastBestTime = System.currentTimeMillis();
			System.out.println("New best fitness on generation " + generation + " with " + (1.0f - allTimeBest)*100 + "% " +
			"image ressemblance. It took " + delta + "ms since last best");
			writeBestFitness(generation, 1.0f - allTimeBest, System.currentTimeMillis());
		}else{
			return bestImage;
		}

		return pop.get(smallest);
	}


	private void writeBestFitness(int gen, float fitness, long time)
	{
		File f = new File("../../../results.ibr");
		try {
			FileOutputStream is = new FileOutputStream(f, true);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);
			w.write(gen + ","+ fitness + "," + time);
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
