% meanVar.m -- version 2011-05-16
%% compute a mean--variance efficient portfolio (long-only)
% generate artificial returns data

PRICES = load('final.txt');

RETURNS=[];
for k = 1: size(PRICES, 1) - 1
    for m = 1 : size(PRICES, 2)
        RETURNS(k, m) = PRICES(k + 1, m) / PRICES(k, m);
    end;
end;

%prepare data for var chart
volatility_var = [];
expected_return_var = [];
skewness_var = [];
kurtosis_var = [];
vectors_var = [];

%number of time frames
ns = size(RETURNS, 1);
%number of assets
na = size(RETURNS, 2);

Q = 2 * cov(RETURNS);

%mean vector
m = mean(RETURNS);

% equality constraints
A = ones(1,na);         
a = 1;           

%number of points
npoints = 50;

%lambda vector from 0 to 1
lambda = sqrt(1-linspace(0.99,0.05,npoints).^2);

%vector
%1000
%0100
%0010
%0001
B = -eye(na); 

%vector
%0
%0
%0
%0
b = zeros(1,na)'; 

% bounds lb <= x <= ub
lb = zeros(na,1); 
ub = ones(na,1);
options = optimset('Algorithm','interior-point-convex');
for i = 1:npoints
    Q = 2*lambda(i) * cov(RETURNS);
    c = -(1-lambda(i)) * m;
    [x,fval] = quadprog(Q,c,B,b,A,a,lb,ub,[],options);
	
	volatility_var(end+1)=volatility(x', RETURNS);
	expected_return_var (end+1) = m*x;
	skewness_var(end+1) = my_skewness(x', RETURNS);
	kurtosis_var(end+1) = my_kurtosis(x', RETURNS);
	vectors_var(end+1,:) = x;
end

%area(volatility_var, vectors_var);
fig = createfigure(volatility_var, expected_return_var, skewness_var, kurtosis_var, vectors_var);
%сохранияем в файл график и данные
file_name = strcat('out/',strrep(num2str(cputime), '.', ''));
saveas(fig, file_name ,'png');
save(file_name);
%exit
%close all;
%clear all;
