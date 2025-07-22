USE [master]
GO
/****** Object:  Database [ClothesShop]    Script Date: 7/22/2025 2:33:18 PM ******/
CREATE DATABASE [ClothesShop]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'ClothesShop', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\ClothesShop.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'ClothesShop_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\ClothesShop_log.ldf' , SIZE = 73728KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [ClothesShop] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [ClothesShop].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [ClothesShop] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [ClothesShop] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [ClothesShop] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [ClothesShop] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [ClothesShop] SET ARITHABORT OFF 
GO
ALTER DATABASE [ClothesShop] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [ClothesShop] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [ClothesShop] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [ClothesShop] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [ClothesShop] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [ClothesShop] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [ClothesShop] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [ClothesShop] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [ClothesShop] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [ClothesShop] SET  ENABLE_BROKER 
GO
ALTER DATABASE [ClothesShop] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [ClothesShop] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [ClothesShop] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [ClothesShop] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [ClothesShop] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [ClothesShop] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [ClothesShop] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [ClothesShop] SET RECOVERY FULL 
GO
ALTER DATABASE [ClothesShop] SET  MULTI_USER 
GO
ALTER DATABASE [ClothesShop] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [ClothesShop] SET DB_CHAINING OFF 
GO
ALTER DATABASE [ClothesShop] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [ClothesShop] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [ClothesShop] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [ClothesShop] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'ClothesShop', N'ON'
GO
ALTER DATABASE [ClothesShop] SET QUERY_STORE = ON
GO
ALTER DATABASE [ClothesShop] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [ClothesShop]
GO
/****** Object:  Table [dbo].[Categories]    Script Date: 7/22/2025 2:33:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Categories](
	[categoryid] [int] IDENTITY(1,1) NOT NULL,
	[categoryname] [nvarchar](30) NULL,
	[type_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[categoryid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[OrderItem]    Script Date: 7/22/2025 2:33:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[OrderItem](
	[order_item_id] [int] IDENTITY(1,1) NOT NULL,
	[quantity] [int] NULL,
	[price] [money] NULL,
	[product_id] [int] NOT NULL,
	[order_id] [int] NOT NULL,
 CONSTRAINT [PK_OrderItem] PRIMARY KEY CLUSTERED 
(
	[order_item_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Orders]    Script Date: 7/22/2025 2:33:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Orders](
	[order_id] [int] IDENTITY(1,1) NOT NULL,
	[orderdate] [datetime] NULL,
	[totalprice] [money] NULL,
	[paymentid] [int] NOT NULL,
	[username] [varchar](30) NOT NULL,
	[status] [bit] NOT NULL,
 CONSTRAINT [PK__Orders__465962299F22AD87] PRIMARY KEY CLUSTERED 
(
	[order_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Payments]    Script Date: 7/22/2025 2:33:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Payments](
	[paymentid] [int] IDENTITY(1,1) NOT NULL,
	[payment_method] [nvarchar](30) NULL,
PRIMARY KEY CLUSTERED 
(
	[paymentid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Products]    Script Date: 7/22/2025 2:33:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Products](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[productname] [nvarchar](max) NOT NULL,
	[supplierid] [int] NULL,
	[categoryid] [int] NULL,
	[size] [varchar](40) NOT NULL,
	[stock] [int] NOT NULL,
	[description] [nvarchar](max) NULL,
	[images] [varchar](255) NOT NULL,
	[colors] [nvarchar](255) NOT NULL,
	[releasedate] [date] NOT NULL,
	[discount] [float] NULL,
	[unitSold] [int] NULL,
	[price] [money] NOT NULL,
	[status] [bit] NOT NULL,
	[typeid] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Suppliers]    Script Date: 7/22/2025 2:33:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Suppliers](
	[supplierid] [int] IDENTITY(1,1) NOT NULL,
	[suppliername] [nvarchar](100) NULL,
	[supplierimage] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[supplierid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Types]    Script Date: 7/22/2025 2:33:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Types](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](100) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 7/22/2025 2:33:18 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[firstname] [nvarchar](30) NOT NULL,
	[lastname] [nvarchar](30) NOT NULL,
	[email] [nvarchar](50) NOT NULL,
	[avatar] [varchar](200) NOT NULL,
	[username] [varchar](30) NOT NULL,
	[password] [varchar](64) NOT NULL,
	[address] [nvarchar](200) NOT NULL,
	[phone] [nvarchar](15) NOT NULL,
	[roleid] [int] NOT NULL,
	[status] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Categories] ON 
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (1, N'Áo sơ mi', 1)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (2, N'T-Shirt', 1)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (3, N'Sweatshirt', 1)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (4, N'Áo khoác', 1)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (5, N'Hoodies', 1)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (6, N'Quần short', 2)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (7, N'Quần thun', 2)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (8, N'Quần jean', 2)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (9, N'Áo Polo', 1)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (10, N'Mũ', 3)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (11, N'Balo', 3)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (12, N'Giày', 3)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (13, N'Áo bóng đá', 1)
GO
INSERT [dbo].[Categories] ([categoryid], [categoryname], [type_id]) VALUES (14, N'Kính', 3)
GO
SET IDENTITY_INSERT [dbo].[Categories] OFF
GO
SET IDENTITY_INSERT [dbo].[OrderItem] ON 
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (1, 9, 249000.0000, 1, 1)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (2, 3, 179000.0000, 2, 2)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (3, 2, 315000.0000, 5, 2)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (4, 4, 699000.0000, 7, 3)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (5, 1, 1596000.0000, 9, 3)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (6, 2, 1569400.0000, 9, 4)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (7, 2, 315000.0000, 5, 4)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (8, 2, 179000.0000, 2, 4)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (9, 2, 1100000.0000, 11, 5)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (10, 1, 875000.0000, 6, 6)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (11, 1, 315000.0000, 5, 6)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (12, 1, 1053000.0000, 10, 7)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (13, 1, 875000.0000, 6, 8)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (14, 1, 875000.0000, 6, 9)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (15, 1, 280500.0000, 8, 10)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (16, 1, 280500.0000, 8, 11)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (17, 2, 280500.0000, 8, 12)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (19, 2, 555000.0000, 12, 14)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (20, 10, 555000.0000, 12, 15)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (25, 2, 1500000.0000, 38, 20)
GO
INSERT [dbo].[OrderItem] ([order_item_id], [quantity], [price], [product_id], [order_id]) VALUES (26, 2, 1500000.0000, 38, 21)
GO
SET IDENTITY_INSERT [dbo].[OrderItem] OFF
GO
SET IDENTITY_INSERT [dbo].[Orders] ON 
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (1, CAST(N'2025-03-10T12:30:00.000' AS DateTime), 2241000.0000, 1, N'tuancsp1505', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (2, CAST(N'2019-05-19T11:30:00.000' AS DateTime), 1167000.0000, 1, N'tuancsp1505', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (3, CAST(N'2019-07-20T11:19:00.000' AS DateTime), 4396000.0000, 1, N'tuancsp1505', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (4, CAST(N'2020-01-19T11:30:00.000' AS DateTime), 2317000.0000, 1, N'user2', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (5, CAST(N'2025-01-19T11:30:00.000' AS DateTime), 2200000.0000, 1, N'user2', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (6, CAST(N'2024-06-10T12:30:00.000' AS DateTime), 1190000.0000, 1, N'tuancsp1505', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (7, CAST(N'2022-02-10T12:30:00.000' AS DateTime), 1053000.0000, 1, N'tuancsp1505', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (8, CAST(N'2021-02-10T12:30:00.000' AS DateTime), 875000.0000, 1, N'tuancsp1505', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (9, CAST(N'2020-02-14T12:30:00.000' AS DateTime), 875000.0000, 1, N'admin123', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (10, CAST(N'2025-07-17T17:59:15.000' AS DateTime), 280500.0000, 1, N'user2', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (11, CAST(N'2025-07-17T18:04:13.000' AS DateTime), 280500.0000, 1, N'user2', 0)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (12, CAST(N'2025-07-17T18:05:40.000' AS DateTime), 561000.0000, 2, N'user2', 0)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (14, CAST(N'2025-07-20T17:35:20.000' AS DateTime), 1110000.0000, 2, N'roblox', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (15, CAST(N'2025-07-20T17:40:44.000' AS DateTime), 5550000.0000, 1, N'roblox', 1)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (20, CAST(N'2025-07-22T00:17:53.000' AS DateTime), 3000000.0000, 1, N'test', 0)
GO
INSERT [dbo].[Orders] ([order_id], [orderdate], [totalprice], [paymentid], [username], [status]) VALUES (21, CAST(N'2025-07-22T00:18:14.000' AS DateTime), 3000000.0000, 1, N'test', 0)
GO
SET IDENTITY_INSERT [dbo].[Orders] OFF
GO
SET IDENTITY_INSERT [dbo].[Payments] ON 
GO
INSERT [dbo].[Payments] ([paymentid], [payment_method]) VALUES (1, N'Tiền mặt')
GO
INSERT [dbo].[Payments] ([paymentid], [payment_method]) VALUES (2, N'Credit Card')
GO
SET IDENTITY_INSERT [dbo].[Payments] OFF
GO
SET IDENTITY_INSERT [dbo].[Products] ON 
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (1, N'ÁO KHOÁC REGULAR TECHNICAL', 6, 4, N'S,M,L', 7, N'Áo sơ mi khoác bằng cotton dệt chéo, có cổ, nẹp khuy liền và cầu vai phía sau. Túi ngực mở, tay dài có nẹp tay áo và măng sét cài khuy cùng vạt tròn.', N'assets/home/img/products/1-1.jpg assets/home/img/products/1-2.jpg', N'Đen', CAST(N'2025-12-01' AS Date), 0.3, 5, 230000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (2, N'ÁO SƠ MI TRƠN TAY NGẮN', 2, 1, N'S,M,L,XXL', 15, N'Áo Sơ Mi Tay Ngắn Nam Cotton Form Regular đem đến item tối giản với phong cách tràn đầy năng lượng, trẻ trung. Áo được làm từ chất liệu cotton với form áo suông, không ôm vào phần cơ thể đem đến sự thoải mái, nhẹ nhàng. Thân áo suông thẳng, thân sau áo có ly tạo nên điểm nổi bật cho áo.', N'assets/home/img/products/2-1.jpg assets/home/img/products/2-2.jpg', N'Trắng,Đen,Xám', CAST(N'2023-02-01' AS Date), 0.37, 76, 179000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (3, N'QUẦN JEANS XANH WASH LASER TÚI SAU FORM SLIM-CROPPED', 6, 8, N'S,M,L', 45, N'Một chiếc jeans xanh Wash Laser túi sau form slim-cropped 4MEN QJ092 trong tủ đồ có thể giúp các chàng trai mix được hàng chục, hàng trăm outfit khác nhau, từ thanh lịch đến bụi bặm cá tính, rồi năng động và tất nhiên luôn toát lên vẻ đẹp trẻ trung và hiện đại. Sở hữu ngay mẫu quần jeans xanh wash laser túi sau form slim-cropped 4MEN QJ092, chất vải mềm mịn và co giãn tốt sẽ rất thích hợp với ai yêu thích jeans.', N'assets/home/img/products/3-1.jpg assets/home/img/products/3-2.jpg', N'Xanh dương', CAST(N'2024-11-01' AS Date), 0, 72, 545000.0000, 1, 2)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (4, N'ÁO HOODIE MAY ĐẮP BASIC FORM REGULAR', 5, 4, N'S,M,L', 30, N'Áo nỉ có mũ, form Regular-Fit; Ngực trái áo có hình thêu chữ sử dụng kỹ thuật đắp vải con giống sắc nét; 2 bên sườn áo may 2 mảng BO đảm bảo đúng form dáng thiết kế và tăng cảm giác thoải mái khi mặc; Áp dụng công nghệ giặt khô trước may hạn chế tình trạng co rút vải.', N'assets/home/img/products/4-1.jpg assets/home/img/products/4-2.jpg', N'Xanh dương', CAST(N'2020-11-01' AS Date), 0.31, 51, 399000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (5, N'ÁO THUN RÃ PHỐI IN HOME IS FORM REGULAR', 6, 2, N'S,M,L', 30, N'Thiết kế áo thun nam basic, cổ tròn form regular tay ngắn trẻ trung, hiện đại. Áo thun nam phối kẻ ngang nam tính, phong cách hiện đại.', N'assets/home/img/products/5-1.jpg assets/home/img/products/5-2.jpg', N'Nâu', CAST(N'2020-11-01' AS Date), 0.17, 21, 315000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (6, N'ÁO SWEATSHIRT BIG LOGO ADIDAS', 1, 3, N'S,M,L,XL', 10, N'Bất kể bạn chuẩn bị tập luyện buổi sáng hay nghỉ ngơi sau một ngày dài, đã có chiếc áo sweatshirt adidas này đồng hành cùng bạn. Chất liệu vải thun da cá siêu dễ chịu cùng cổ tay và gấu áo bo gân giúp bạn luôn thoải mái và duy trì nhiệt độ hoàn hảo trong mọi hoạt động. Hãy diện chiếc áo này và sẵn sàng cho tất cả.', N'assets/home/img/products/6-1.jpg assets/home/img/products/6-2.jpg', N'Xám,Trắng', CAST(N'2023-11-01' AS Date), 0.15, 11, 875000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (7, N'ÁO BÓNG ĐÁ NIKE LFC M NK SSL SWOOSH TEE NAM DZ3613-010', 2, 13, N'M,L', 29, N'Chất liệu cotton mềm, nhẹ. In đồ họa tương phản với mặt trước. Cổ thuyền với tay áo ngắn. In thương hiệu logo swoosh của Nike.', N'assets/home/img/products/7-1.jpg assets/home/img/products/7-2.jpg', N'Đen', CAST(N'2023-11-01' AS Date), 0, 22, 699000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (8, N'QUẦN JOGGER THUN RÃ PHỐI FORM REGULAR', 3, 7, N'S,M,L,XL', 22, N'Với xu hướng bùng nổ thời trang thể thao và thời trang đường phố hiện nay thì với Quần Jogger Nam – đại diện cho phong cách Street Style ngày càng được ưa chuộng. Đặc biệt, để phù hợp cho môi trường đi làm thì Kaki là chất liệu được đánh giá là lịch sự và trang trọng hơn hẳn chất liệu thun hay nỉ. Vì vậy mà bạn có thể tự tin diện Quần Jogger Kaki vừa để đi làm vừa để đi chơi.', N'assets/home/img/products/8-1.jpg assets/home/img/products/8-2.jpg', N'Vàng', CAST(N'2025-05-01' AS Date), 0.34, 35, 425000.0000, 1, 2)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (9, N'ÁO KHOÁC GOLF ADIDAS HYBRID-SPACER', 1, 4, N'S,M,L', 28, N'Bắt đầu buổi chơi trong phong cách thanh thoát với chiếc áo khoác golf adidas này. Cấu trúc hybrid kết hợp hai lớp vải được bố trí hợp lý để giữ ấm, cùng độ co giãn tăng cường tại những vị trí cần thiết nhất để bạn vận động tối ưu trên sân golf. Các túi giúp giữ ấm đôi tay giữa những cú đánh và cất các vật dụng nhỏ khi chơi.', N'assets/home/img/products/9-1.jpg assets/home/img/products/9-2.jpg', N'Đen,Nâu', CAST(N'2025-06-01' AS Date), 0.41, 53, 2660000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (10, N'ÁO BÓNG ĐÁ NIKE AS M NK DF FC LIBERO HOODIE', 2, 13, N'S,M,L,XL', 20, N'Áo bóng đá nike AS M NK DF FC LIBERO HOODIE nam DC9076-010', N'assets/home/img/products/10-1.jpg assets/home/img/products/10-2.jpg', N'Đen', CAST(N'2023-12-01' AS Date), 0, 5, 1053000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (11, N'TRAVIS SCOTT CACT.US CORP X NIKE U NRG BH LONG SLEEVE T', 2, 3, N'S', 15, N'Hoạt động trong nhà/ngoài trời.', N'assets/home/img/products/11-1.jpg assets/home/img/products/11-2.jpg', N'Đen', CAST(N'2023-12-01' AS Date), 0, 5, 1053000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (12, N'ÁO THUN OVERSIZED *RETRO 9AS*', 5, 2, N'S,M,L', 33, N'100% COTTON, 320GSM, IN LỤA THỦ CÔNG, OVERSIZED FIT', N'assets/home/img/products/12-1.jpg assets/home/img/products/12-2.jpg', N'Trắng', CAST(N'2025-01-01' AS Date), 0, 72, 555000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (13, N'QUẦN JEANS THIÊN THẦN PHUN SƠN', 5, 8, N'S,M,L', 30, N'VẢI CHÍNH: 100% SỢI BÔNG – COTTON, MẪU CAO 1M76 68KG MẶC SIZE 32', N'assets/home/img/products/13-1.jpg assets/home/img/products/13-2.jpg', N'Xanh dương', CAST(N'2020-11-01' AS Date), 0.31, 51, 325000.0000, 1, 2)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (14, N'ÁO TẬP TRƯỚC TRẬN MANCHESTER UNITED STONE ROSES', 1, 13, N'M', 30, N'Chiếc áo đấu này hình thành mối liên kết vững chắc với sức ảnh hưởng lâu bền của Manchester trên toàn cầu. Vay mượn các yếu tố thiết kế từ một cây cầu gần thời Cách mạng công nghiệp, mặt trước áo phủ họa tiết hình học lấy cảm hứng từ Hoa Hồng Lancashire. Công nghệ AEROREADY đánh bay mồ hôi và các chi tiết đội tuyển siêu nhẹ phù hợp với sân cỏ — bất kể bạn chuẩn bị thi đấu quốc tế hay đá giao hữu đội hình sân 5.', N'assets/home/img/products/14-1.jpg assets/home/img/products/14-2.jpg', N'Đỏ', CAST(N'2025-02-01' AS Date), 0.17, 21, 2200000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (15, N'ÁO THUN NAM THỂ THAO Z.N.E', 1, 2, N'S,M,L', 10, N'Với sự cung cấp năng lượng tích cực, chiếc áo thun adidas này được thiết kế để giúp bạn cảm thấy tốt nhất. Được làm từ chất liệu mềm mại và cắt dáng rộng rãi, nó giúp bạn tách biệt khỏi những lo lắng hàng ngày và nạp năng lượng lại. Kết hợp với quần short len hoặc quần jogger để có sự thoải mái từ đầu đến chân và tạo cảm giác bình yên. Bằng cách lựa chọn vật liệu tái chế, chúng tôi có thể tái sử dụng các vật liệu đã được tạo ra, giúp giảm thiểu lượng rác thải. Các lựa chọn vật liệu tái tạo sẽ giúp chúng tôi loại bỏ sự phụ thuộc của chúng tôi vào các nguồn tài nguyên hữu hạn. Các sản phẩm của chúng tôi được làm từ một hợp chất của các vật liệu tái chế và tái tạo có ít nhất 70% tổng số của các vật liệu này.', N'assets/home/img/products/15-1.jpg assets/home/img/products/15-2.jpg', N'Vàng', CAST(N'2023-11-01' AS Date), 0.2, 12, 950000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (16, N'QUẦN SHORT JEANS ĐEN TÚI CHÉO WASH BẠC', 6, 6, N'M,L', 30, N'Phong cách: thể thao, Hàn Quốc, đường phố', N'assets/home/img/products/16-1.jpg assets/home/img/products/16-2.jpg', N'Đen', CAST(N'2023-11-01' AS Date), 0, 21, 415000.0000, 1, 2)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (17, N'QUẦN SHORTS THỂ THAO 3 SỌC', 2, 6, N'S,M,L,XL', 30, N'Không gì có thể khiến bạn phân tâm khỏi thực tại khi bạn đang diện chiếc quần short adidas này. Mềm mại và siêu nhẹ, đôi chân bạn sẽ được bao bọc trong sự thoải mái, còn 3 Sọc biểu tượng tạo điểm nhấn tinh tế. Là lựa chọn hoàn hảo để dạo phố ban ngày cũng như ngắm sao về đêm, chiếc quần short này sẽ đồng hành cùng bạn từ lúc bắt đầu cho đến sau đó, sau đó, sau đó nữa...', N'assets/home/img/products/17-1.jpg assets/home/img/products/17-2.jpg', N'Đen', CAST(N'2025-01-01' AS Date), 0.5, 61, 1050000.0000, 1, 2)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (18, N'ÁO THUN RAGLAN IN NGỰC BO CỔ DỆT', 2, 2, N'S,L', 30, N'Phong cách: thể thao, Hàn Quốc, đường phố, công sở', N'assets/home/img/products/18-1.jpg assets/home/img/products/18-2.jpg', N'Đỏ', CAST(N'2023-11-01' AS Date), 0.41, 51, 412000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (19, N'ÁO POLO CỔ V PHỐI IN HỌA TIẾT', 6, 9, N'S,M,L', 30, N'Phong cách: cơ bản, đường phố, Hàn Quốc.', N'assets/home/img/products/19-1.jpg assets/home/img/products/19-2.jpg', N'Đen', CAST(N'2025-03-01' AS Date), 0.11, 11, 345000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (20, N'ÁO THUN OVERSIZED *RETRO 9AS*', 5, 2, N'S,M,L,XL', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/20-1.jpg assets/home/img/products/20-2.jpg', N'Trắng', CAST(N'2025-05-01' AS Date), 0, 81, 550000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (21, N'ÁO THUN DÀI TAY *CLOUD*', 5, 2, N'S,M,L,XL', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/21-1.jpg assets/home/img/products/21-2.jpg', N'Trắng', CAST(N'2025-02-01' AS Date), 0, 81, 700000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (22, N'ÁO SƠ MI OXFORD *ANGEL*', 5, 1, N'XS,S,M,L,XL', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/22-1.jpg assets/home/img/products/22-2.jpg', N'Xanh dương', CAST(N'2025-04-01' AS Date), 0, 81, 500000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (23, N'SƠ MI DÀI TAY *GLOWING HEART*', 1, 2, N'S,M,L', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/23-1.jpg assets/home/img/products/23-2.jpg', N'Trắng,Đen', CAST(N'2025-03-01' AS Date), 0, 81, 650000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (24, N'ÁO KHOÁC DÙ *ANGELS*', 4, 4, N'S,M,L', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/24-1.jpg assets/home/img/products/24-2.jpg', N'Xanh lá,Đen', CAST(N'2025-06-01' AS Date), 0, 81, 950000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (25, N'ÁO KHOÁC DÙ *LAST JUDGEMENT*', 4, 4, N'S,M,L', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/25-1.jpg assets/home/img/products/25-2.jpg', N'Xanh lá,Đen', CAST(N'2024-02-01' AS Date), 0, 81, 1500000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (26, N'QUẦN DÀI *ANGEL BOBUI*', 5, 7, N'M,L,XL', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/26-1.jpg assets/home/img/products/26-2.jpg', N'Xanh lá', CAST(N'2024-03-02' AS Date), 0.1, 81, 700000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (27, N'QUẦN DOUBLE KNEE CANVAS WORKWEAR', 5, 7, N'S,M,L', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/27-1.jpg assets/home/img/products/27-2.jpg', N'Vàng', CAST(N'2023-04-02' AS Date), 0.2, 81, 750000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (28, N'GIÀY CHELSEA BOOTS ALL BLACK', 2, 12, N'41,42,43,44', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/28-1.jpg assets/home/img/products/28-2.jpg assets/home/img/products/29-2.jpg', N'Đen', CAST(N'2023-04-02' AS Date), 0.77, 11, 1040000.0000, 1, 3)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (29, N'Túi Đeo Chéo Nam Adidas Airliner Bag Archive HY4320', 1, 11, N'21cm x 5.5cm x 16cm', 10, N'Túi Đeo Chéo Nam Adidas Airliner Bag Archive HY4320 Màu Đen là chiếc túi cao cấp đến từ thương hiệu Adidas nỗi tiếng của Đức. Nếu chiếc túi airliner này trông có vẻ quen thuộc, thì có lẽ là vì đây là một trong những thiết kế đặc trưng trong bộ sưu tập adidas. Nhưng phiên bản hiện đại này mang đến nét thanh lịch mới mẻ cho thiết kế nguyên bản.', N'assets/home/img/products/29-1.jpg', N'Đen', CAST(N'2023-04-02' AS Date), 0.3, 11, 1290000.0000, 1, 3)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (30, N'Túi Đeo Chéo Adidas Festival', 1, 11, N'4cm x 14cm x 16cm', 10, N'Túi Đeo Chéo Adidas Must Haves Seasonal Small Bag HY3030 Màu Đen là chiếc túi cao cấp đến từ thương hiệu Adidas nỗi tiếng của Đức. Nếu chiếc túi airliner này trông...', N'assets/home/img/products/30-1.jpg', N'Đen', CAST(N'2023-04-02' AS Date), 0.4, 11, 1250000.0000, 1, 3)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (31, N'Mũ Nam Louis Vuitton', 3, 10, N'L,M', 0, N'Mũ Nam Louis Vuitton LV Monogram Constellation Cap M7136M Màu Xanh Denim là chiếc mũ thời trang dành cho nam đến từ thương hiệu Louis Vuitton nổi tiếng.', N'assets/home/img/products/31-1.jpg assets/home/img/products/31-2.jpg', N'Xanh đen', CAST(N'2023-04-02' AS Date), 0.06, 15, 550000.0000, 1, 3)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (32, N'Áo Sơmi Nike FC Dri-Fit DA1474-100', 2, 1, N'S,M,L,XXL', 15, N'Áo Sơmi Nike FC Dri-Fit đầy cá tính để làm phong phú tủ đồ của bạn.', N'assets/home/img/products/32-1.jpg', N'Trắng', CAST(N'2023-02-01' AS Date), 0.37, 76, 1090000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (33, N'M ALL SZN VAL H', 1, 4, N'S,M,L', 30, N'Áo nỉ có mũ; Ngực trái áo có hình thêu logo adidas; 2 bên sườn áo may 2 mảng BO đảm bảo đúng form dáng thiết kế và tăng cảm giác thoải mái khi mặc;', N'assets/home/img/products/33-1.jpg', N'Đỏ', CAST(N'2020-11-01' AS Date), 0.31, 51, 1399000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (34, N'Mũ Lưỡi Trai Aerogram', 3, 10, N'Free', 30, N'Với thiết kế sang trọng với tông chủ đạo là màu đen cùng với đó có một logo nổi ngay bên trái chiếc mũ.', N'assets/home/img/products/34-1.jpg', N'Đen', CAST(N'2025-03-01' AS Date), 0.34, 31, 425000.0000, 1, 2)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (35, N'Áo Nike Men’s Max90 Basketball T-Shirt', 2, 2, N'S,L,XL', 30, N'Phong cách: thể thao, Hàn Quốc, đường phố', N'assets/home/img/products/35-1.jpg', N'Đen', CAST(N'2023-11-01' AS Date), 0.41, 51, 412000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (36, N'ÁO HOODIE KHÓA KÉO STREET NEUCLASSICS', 1, 5, N'S,M,L', 30, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/36-1.jpg', N'Xanh dương', CAST(N'2025-03-01' AS Date), 0, 81, 650000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (37, N'KÍNH MÁT DÁNG CHỮ NHẬT', 4, 14, N'Free', 27, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/37-1.jpg', N'Đen', CAST(N'2025-01-01' AS Date), 0, 83, 950000.0000, 1, 1)
GO
INSERT [dbo].[Products] ([id], [productname], [supplierid], [categoryid], [size], [stock], [description], [images], [colors], [releasedate], [discount], [unitSold], [price], [status], [typeid]) VALUES (38, N'KÍNH MÁT DÁNG BẦU DỤC', 4, 14, N'Free', 26, N'Phong cách: trẻ trung, thời thượng, đường phố.', N'assets/home/img/products/38-1.jpg', N'Xanh dương,Nâu,Đỏ', CAST(N'2024-02-01' AS Date), 0, 85, 1500000.0000, 1, 1)
GO
SET IDENTITY_INSERT [dbo].[Products] OFF
GO
SET IDENTITY_INSERT [dbo].[Suppliers] ON 
GO
INSERT [dbo].[Suppliers] ([supplierid], [suppliername], [supplierimage]) VALUES (1, N'Adidas', N'assets/home/img/suppliers/1.jpg')
GO
INSERT [dbo].[Suppliers] ([supplierid], [suppliername], [supplierimage]) VALUES (2, N'Nike', N'assets/home/img/suppliers/2.jpg')
GO
INSERT [dbo].[Suppliers] ([supplierid], [suppliername], [supplierimage]) VALUES (3, N'Louis Vuitton', N'assets/home/img/suppliers/3.jpg')
GO
INSERT [dbo].[Suppliers] ([supplierid], [suppliername], [supplierimage]) VALUES (4, N'Channel', N'assets/home/img/suppliers/4.jpg')
GO
INSERT [dbo].[Suppliers] ([supplierid], [suppliername], [supplierimage]) VALUES (5, N'BoBui', N'assets/home/img/suppliers/5.jpg')
GO
INSERT [dbo].[Suppliers] ([supplierid], [suppliername], [supplierimage]) VALUES (6, N'4MEN', N'assets/home/img/suppliers/6.jpg')
GO
SET IDENTITY_INSERT [dbo].[Suppliers] OFF
GO
SET IDENTITY_INSERT [dbo].[Types] ON 
GO
INSERT [dbo].[Types] ([id], [name]) VALUES (1, N'Áo')
GO
INSERT [dbo].[Types] ([id], [name]) VALUES (2, N'Quần')
GO
INSERT [dbo].[Types] ([id], [name]) VALUES (3, N'Phụ kiện')
GO
SET IDENTITY_INSERT [dbo].[Types] OFF
GO
SET IDENTITY_INSERT [dbo].[Users] ON 
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (2, N'admin', N'123', N'admin@gmail.com', N'assets/home/img/users/1.jpg', N'admin123', N'e10adc3949ba59abbe56e057f20f883e', N'Cần Thơ, Việt Nam', N'0815255855', 1, 1)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (9, N'Minh', N'Tien', N'minhtien@gmail.com', N'assets/home/img/users/iso.jpg', N'roblox', N'e10adc3949ba59abbe56e057f20f883e', N'Đồng Tháp', N'0815255855', 2, 1)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (11, N'Shin', N'nosuke', N'shin@gmail.com', N'assets/home/img/users/iso.jpg', N'shin', N'e10adc3949ba59abbe56e057f20f883e', N'Ninh Kieu', N'0815255855', 2, 1)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (17, N'test', N'test', N'test@123', N'assets/home/img/users/anhtuan1.jpg', N'test', N'e10adc3949ba59abbe56e057f20f883e', N'test', N'0812323223', 2, 1)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (18, N'test1', N'test1', N'test1@123', N'assets/home/img/users/user.jpg', N'test1', N'e10adc3949ba59abbe56e057f20f883e', N'', N'', 2, 0)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (21, N'hello', N'test', N'test@1234', N'assets/home/img/users/user.jpg', N'test2', N'e10adc3949ba59abbe56e057f20f883e', N'', N'', 2, 1)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (22, N'test', N'3', N'test3@333', N'assets/home/img/users/user.jpg', N'test3', N'e10adc3949ba59abbe56e057f20f883e', N'', N'', 2, 1)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (24, N'test', N'5', N'test5@123', N'', N'test5', N'e10adc3949ba59abbe56e057f20f883e', N'VN', N'0815255866', 2, 0)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (3, N'Anh', N'Tuan', N'tuanlace180905@fpt.edu.vn', N'assets/home/img/users/anhtuan.jpg', N'tuancsp1505', N'e10adc3949ba59abbe56e057f20f883e', N'Ninh Kiều, Cần Thơ, Việt Nam', N'0815255855', 2, 1)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (1, N'Jung', N'Kim', N'user@gmail.com', N'assets/home/img/users/user.jpg', N'user1', N'e10adc3949ba59abbe56e057f20f883e', N'Ha Noi', N'0981347469', 2, 0)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (4, N'Bé', N'Iu', N'beiudethuong@gmail.com', N'assets/home/img/users/iso.jpg', N'user2', N'e10adc3949ba59abbe56e057f20f883e', N'Cần Thơ, Việt Nam', N'0666666666', 2, 1)
GO
INSERT [dbo].[Users] ([id], [firstname], [lastname], [email], [avatar], [username], [password], [address], [phone], [roleid], [status]) VALUES (5, N'User', N'3', N'user3@gmail.com', N'assets/home/img/users/user3.jpg', N'user3', N'e10adc3949ba59abbe56e057f20f883e', N'VN', N'0123456789', 2, 1)
GO
SET IDENTITY_INSERT [dbo].[Users] OFF
GO
ALTER TABLE [dbo].[Categories]  WITH CHECK ADD FOREIGN KEY([type_id])
REFERENCES [dbo].[Types] ([id])
GO
ALTER TABLE [dbo].[OrderItem]  WITH CHECK ADD  CONSTRAINT [FK__OrderItem__order__4BAC3F29] FOREIGN KEY([order_id])
REFERENCES [dbo].[Orders] ([order_id])
GO
ALTER TABLE [dbo].[OrderItem] CHECK CONSTRAINT [FK__OrderItem__order__4BAC3F29]
GO
ALTER TABLE [dbo].[OrderItem]  WITH CHECK ADD  CONSTRAINT [FK__OrderItem__produ__4AB81AF0] FOREIGN KEY([product_id])
REFERENCES [dbo].[Products] ([id])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[OrderItem] CHECK CONSTRAINT [FK__OrderItem__produ__4AB81AF0]
GO
ALTER TABLE [dbo].[Orders]  WITH CHECK ADD  CONSTRAINT [FK__Orders__paymenti__46E78A0C] FOREIGN KEY([paymentid])
REFERENCES [dbo].[Payments] ([paymentid])
GO
ALTER TABLE [dbo].[Orders] CHECK CONSTRAINT [FK__Orders__paymenti__46E78A0C]
GO
ALTER TABLE [dbo].[Orders]  WITH NOCHECK ADD  CONSTRAINT [FK__Orders__username__47DBAE45] FOREIGN KEY([username])
REFERENCES [dbo].[Users] ([username])
GO
ALTER TABLE [dbo].[Orders] CHECK CONSTRAINT [FK__Orders__username__47DBAE45]
GO
ALTER TABLE [dbo].[Products]  WITH CHECK ADD FOREIGN KEY([categoryid])
REFERENCES [dbo].[Categories] ([categoryid])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[Products]  WITH CHECK ADD FOREIGN KEY([supplierid])
REFERENCES [dbo].[Suppliers] ([supplierid])
ON UPDATE CASCADE
ON DELETE SET NULL
GO
ALTER TABLE [dbo].[Products]  WITH CHECK ADD FOREIGN KEY([typeid])
REFERENCES [dbo].[Types] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
USE [master]
GO
ALTER DATABASE [ClothesShop] SET  READ_WRITE 
GO
