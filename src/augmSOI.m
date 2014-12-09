xhat = [0 ; 0];
vhat = 0;
e = 0;
y = 0;

Gp = ss(A,B,C,D); %transfer function of the process
Hp = c2d(Gp,h);
[Phi, Gamma] = ssdata(Hp);

%state feedback design
pod = exp(roots(fPole)*h);
L = place(Phi,Gamma,pod)
Lc = 1/(C*inv(eye(size(A,1))-Phi+Gamma*L)*Gamma);

%augmented disturbance observer design
pcd = exp(roots(oPole)*h); 
Phie = [Phi Gamma; zeros(1,size(A,1)) 1];
Gamnew = [Gamma ; 0];
Ce = [C 0];
Ke = place(Phie',Ce',pcd)'
K = [Ke(1:size(A,1))];
Kw = Ke(size(A,1)+1);

Anew = [Phi-Gamma*L-K*C zeros(size(A,1),1) zeros(size(A,1),1); -Kw*C 1 0; -C 0 0];
By = [K;Kw;1];
Br = [Gamma*Lc ;0 ;0];

Gpr = ss(Anew,By,[C 0 0],0,h);
