close all;
fileID= fopen('Mat2.txt','r');
fileID2=fopen('Mat.txt','r');
formatSpec= '%f\n';
A=fscanf(fileID,formatSpec);
B=fscanf(fileID2,formatSpec);

figure();
plot(A);

figure();
plot(B);