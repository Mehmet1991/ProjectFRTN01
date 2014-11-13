A = [0 1 0 0; -174.67 -1.36 174.67 0; 0 0 0 1; 195.6947 0 -195.6947 -1.82];
B = [0; 1.29257; 0; 0];
C = [0 0 280 0];
D = 0;

omegaa = 2;
zetaa = 0.866;
omegab = 2;
zetab = 0.866;

omegac = 1;
zetac = 0.866;
omegad = 1;
zetad = 0.866;

omegae = 1;

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
pc = conv([1 2*omegaa*zetaa omegaa^2],[1 2*omegab*zetab omegab^2]); %control poles in continuous time
pc = conv(pc, [1 omegae]);
pc = roots(pc);
pcd = exp(pc*h); %in discrete time
Le = place(Anew, Bnew, pcd);
L = Le(1:size(Anew,1)-1);
li = Le(size(Anew,1));

%observer design
po = roots(conv([1 2*omegac*zetac omegac^2],[1 2*omegad*zetad omegad^2])); %observer poles in continuous time
pod = exp(po*h);
K = place(Phi', C', pod)';

%reference handling
Hcl = ss(Anew-Bnew*Le, Bnew, Cnew, 0, h);
Lc = 1/dcgain(Hcl);

%form the controller (observer + state feedback + integral action)
AR = [Phi-Gam*L-K*C -Gam*li; zeros(1,size(Phi,1)) 1];
BRy = [K; 1];
BRr = [Gam*Lc; 0];
CR = [-L -li];
DRr = Lc;
DRy = 0;

Gr = -ss(AR,BRy,CR,DRy); %transfer function from -y to u
