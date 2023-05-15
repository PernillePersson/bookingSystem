package com.example.bookingsystem.model.objects;


import javafx.concurrent.Task;
import javafx.scene.image.Image;
import java.io.File;
import java.util.List;

public class SlideImage extends Task<Image>{

    private final List<File> images;

    private int index;

    public SlideImage(List<File> images, int index){
        this.images = images;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    protected Image call() throws Exception {

        while (!isCancelled()) {
            Image slideshowImage = new Image(images.get(index).toURI().toString());
            updateValue(slideshowImage);

            try {
                Thread.sleep(10 * 1000L);
            } catch (InterruptedException i) {
                if (isCancelled()) {
                    break;
                }
            }
            index = (index + 1) % images.size();
        }
        return null;
    }
}

