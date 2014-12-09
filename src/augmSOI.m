%WT
A = [-0.0502 0 ; 0.0502 -0.0502];
B = [0.2500 ; 0];
C = [0 1];
D = [0];

%DC
%A = [-0.12 0 ; 5 0];
%B = [2.25 ; 0];
%C = [0 1];
%D = [0];
h = 0.1;

polesDCstate = roots([1 -1.6 0.65]);
polesDCobs = roots([1 -1.75 1.06 -0.22]);

polesWTstate = roots([1 1.1025 0.0506]);
polesWTobs = roots([1 1.4400 0.4563 0.0297]);

xhat = [0 ; 0];

vhat = 0;
e = 0;
y = 0;

Gp = ss(A,B,C,D); %transfer function of the process

Hp = c2d(Gp,h);

[Phi, Gamma] = ssdata(Hp);

%state feedback design

w2 =1.5*0.15;
zeta2 = 0.7;
po = roots([1 2*zeta2*w2 w2^2]);

pod = exp(po*h);
L = place(Phi,Gamma,pod)
%L = place(Phi,Gamma,polesDCstate)
Lc = 1/(C*inv(eye(size(A,1))-Phi+Gamma*L)*Gamma);

%augmented disturbance observer design

a = 1;
w = 0.15;
zeta = 0.7;
s2kv = a*w + 2*zeta*w;
skv = 2*a*zeta*w^2 + w^2;
konst = a*w^3;

pc = [1 s2kv skv konst];
pc = roots(pc);
pcd = exp(pc*h); 

Phie = [Phi Gamma; zeros(1,size(A,1)) 1];
Gamnew = [Gamma ; 0];
Ce = [C 0];
Ke = place(Phie',Ce',pcd)'
%Ke = place(Phie',Ce',polesDCobs)'
K = [Ke(1:size(A,1))];
Kw = Ke(size(A,1)+1);



Anew = [Phi-Gamma*L-K*C zeros(size(A,1),1) zeros(size(A,1),1); -Kw*C 1 0; -C 0 0];
By = [K;Kw;1];
Br = [Gamma*Lc ;0 ;0];

Gpr = ss(Anew,By,[C 0 0],0,h);
stepplot(Gpr)

