A = [ -0.1004 -0.0403 ; 0.0625 0];
B = [0.5 ; 0];
C = [0 0.4016];
D = 0;


% Design of state feedback and integral action system
Ae = [A zeros(2,1); -C 0]; 
Be = [B; 0]; 

a = 0.9;
w = 0.115;
zeta = 0.75;



w2 =1.45*0.115;
zeta2 = 0.75;


s2kv = a*w + 2*zeta*w;
skv = 2*a*zeta*w^2 + w^2;
konst = a*w^3;

xhat = zeros(size(A,1),1);
xi = 0;
y = 1;
uc = 0;


pc = roots([1 s2kv skv konst]);
Le = place(Ae,Be,pc); 
L = Le(1:size(A,1));
li = Le(size(A,1)+1);
lr = 0; % direct coupling from reference value, not needed since use of integral action


% Design of observer

po = roots([1 2*zeta2*w2 w2^2]);
K = place(A',C',po)'; % calculate K vector

% Design of SOI controller system, the total
AR = [A-B*L-K*C -B*li; zeros(1,size(A,1)) 0];
BRy = [K; -1];
BRr = [B*lr; 1];
CR = [-L -li];
DRy = 0;
DRr = lr;
Gr = -ss(AR,BRy,CR,DRy); 

h = 0.2;
Hp = c2d(Gr,h); % sample and discretize the controller

[AR2 BRy2 CR2 DRy2] = ssdata(Hp); %get the discretized matrices


T = 0:0.2:10;       % simulation time = 10 seconds
U = ones(size(T)); % u = 1; this is a step input
[Y, Tsim, X] = lsim(Hp,U,T);
figure(1)
plot(Tsim,Y) %plot the stepresponse
