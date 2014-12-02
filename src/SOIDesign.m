%A = [  -0.1004   -0.0403 ; 0.0625  0];
%B = [0.5 ; 0];
%C = [0 0.4016];
%D = 0;

A = [-0.0502 0 ; 0.0502 -0.0502]; 
B = [0.2500 ; 0];
C = [0 1];
D = [0];

a = 1;
w = 0.15;
zeta = 0.7;

a2 = 1;
w2 =2* 0.15;
zeta2 = 0.7;


s2kv = a*w + 2*zeta*w;
skv = 2*a*zeta*w^2 + w^2;
konst = a*w^3;

s2kv2 = a2*w2 + 2*zeta2*w2;
skv2 = 2*a2*zeta2*w2^2 + w2^2;
konst2 = a2*w2^3;

xhat = zeros(size(A,1),1);
xi = 0;
y = 1;
uc = 0;

%omegae = 1;

Gp = ss(A,B,C,D); %transfer function of the process

%sample the process
h = 0.2; 
Hp = c2d(Gp,h);
[Phi, Gam] = ssdata(Hp);

%integral action system
Anew = [Phi zeros(size(Phi,1),1); C eye(size(C,1))];
Bnew = [Gam; 0];
Cnew = [C 0];

%state feedback and integral action design
%pc = conv([1 2*omegaa*zetaa omegaa^2],[1 2*omegab*zetab omegab^2]); %control poles in continuous time
%pc = conv(pc, [1 omegae]);



pc = [1 s2kv skv konst];



pc = roots(pc);
pcd = exp(pc*h); %in discrete time
Le = place(Anew, Bnew, pcd);
L = Le(1:size(Anew,1)-1);
li = Le(size(Anew,1));

%observer design
%po = roots(conv([1 2*omegac*zetac omegac^2],[1 2*omegad*zetad omegad^2])); %observer poles in continuous time
po = roots([1 2*zeta2*w2 w2^2]);
pod = exp(po*h);
K = place(Phi', C', pod)';

%reference handling
%Hcl = ss(Anew-Bnew*Le, Bnew, Cnew, 0, h);
%Lc = 1/dcgain(Hcl);
Lc = 0;

%form the controller (observer + state feedback + integral action)
AR = [Phi-Gam*L-K*C -Gam*li; zeros(1,size(Phi,1)) 1];
BRy = [K; 1];
BRr = [Gam*Lc; 0];
CR = [-L -li];
DRr = Lc;
DRy = 0;

Gr = -ss(AR,BRy,CR,DRy); %transfer function from -y to u
