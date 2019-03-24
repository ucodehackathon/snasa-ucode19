function [Ni,Nf,Fs,nuevo] = timeobt(video)

[signal,Fs] = audioread(video);
s = signal(:,1);

Ns = length(s);
i = 1;
n = 1:Ns;
for n = 1:1:Ns
    if s(n) > 0.4
        max(i) = n;
        i = i+1;
    end
end
Ni = max(1);
Nf = max(length(max));

nuevo=s(Ni:Nf);
end

