function [Ni,Nf,Fs,nuevo] = datatext(video)
[Ni,Nf,Fs,nuevo] = timeobt(video);
formatSpec = '%d,%d\n';
datatext = fopen('audiodata.txt','w');
fprintf(datatext,formatSpec,Ni,Nf);
end

