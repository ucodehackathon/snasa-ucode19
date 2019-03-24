function [audio] = soundobt(video)
audio = 'adidasSound.mp3';
videor = vision.VideoFileReader(video,'AudioOutputPort',true);
videow = vision.VideoFileWriter(audio,'AudioInputPort',true);
while ~isDone(videor)
      audioFrame = step(videor);
      step(videow,audioFrame);
end
release(videow);
release(videor);
end

